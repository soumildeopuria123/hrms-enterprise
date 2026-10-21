import React, { useEffect, useState } from "react";
import {
  Card,
  Button,
  Badge,
  Input,
  Label,
  Select,
  Option,
  Table,
  TableHeader,
  TableRow,
  TableCell,
} from "@/components/neo-brutalist"; // adjust import path
import axios from "axios";
import { useAuth } from "@/hooks/useAuth";

interface PayrollRunSummary {
  id: number;
  payrollRunId: string;
  periodStart: string;
  periodEnd: string;
  payDate: string;
  status: string;
}

interface PayrollItem {
  employeeName: string;
  grossAmount: number;
  deductionsAmount: number;
  netAmount: number;
  currency: string;
  calculationReference: string;
}

const statusBadgeMap: Record<string, string> = {
  Draft: "bg-gray-100 text-gray-800 border-gray-200",
  Processing: "bg-yellow-100 text-yellow-800 border-yellow-200",
  Finalized: "bg-blue-100 text-blue-800 border-blue-200",
  Approved: "bg-green-100 text-green-800 border-green-200",
};

const PayrollManagementPage: React.FC = () => {
  const { token, employeeId, role } = useAuth();
  const [runs, setRuns] = useState<PayrollRunSummary[]>([]);
  const [selectedRun, setSelectedRun] = useState<PayrollRunSummary | null>(null);
  const [items, setItems] = useState<PayrollItem[]>([]);
  const [modalOpen, setModalOpen] = useState(false);

  useEffect(() => {
    fetchRuns();
  }, [token, role]);

  const fetchRuns = async () => {
    try {
      let url = "/api/payroll/runs";
      if (role === "EMPLOYEE" && employeeId) {
        url += `?employeeId=${employeeId}`;
      } else if (role === "ADMIN") {
        url += "?tenantId=" + localStorage.getItem("tenantId");
      }
      const res = await axios.get(url, {
        headers: { Authorization: `Bearer ${token}` },
      });
      setRuns(res.data);
    } catch (err) {
      console.error("Failed to fetch payroll runs", err);
    }
  };

  const loadItems = async (runId: string) => {
    try {
      const res = await axios.get(`/api/payroll/runs/${runId}/items`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      setItems(res.data);
    } catch (err) {
      console.error("Failed to fetch payroll items", err);
    }
  };

  return (
    <Card p="6" maxWidth="900px">
      <h2 className="mb-4 text-2xl font-bold border-b pb-2">Payroll Management</h2>

      <Card p="4" mt="2" maxWidth="100%">
        <h3 className="mb-3 text-lg font-semibold border-b pb-2">Payroll Runs</h3>
        {role === "EMPLOYEE" && employeeId ? (
          <p className="text-muted-inline">Employees view their own payslips.</p>
        ) : null}
        <Table>
          <TableHeader>
            <TableRow>
              <TableCell className="text-left py-2">Period</TableCell>
              <TableCell className="text-left py-2">Pay Date</TableCell>
              <TableCell className="text-left py-2">Status</TableCell>
              <TableCell className="text-left py-2">Total Payout</TableCell>
            </TableRow>
          </TableHeader>
          <TableBody>
            {runs.map((run) => (
              <TableRow key={run.id} className="border-b">
                <TableCell className="py-3">
                  {run.periodStart} – {run.periodEnd}
                </TableCell>
                <TableCell className="py-3">{run.payDate}</TableCell>
                <TableCell className="py-3">
                  <Badge className={statusBadgeMap[run.status]}>{run.status}</Badge>
                </TableCell>
                <TableCell className="py-3">
                  {items.some((i) => i.calculationReference.includes(run.payrollRunId))
                    ? `${items.reduce((sum, i) => sum + i.netAmount, 0).toLocaleString()} ${items[0].currency}`
                    : "—"}
                </TableCell>
              </TableRow>
            ))}
            {role !== "EMPLOYEE" && runs.length === 0 && (
              <TableRow>
                <TableCell colSpan={4} className="py-3 text-center text-muted-inline">
                  No payroll runs found.
                </TableCell>
              </TableRow>
            )}
          </TableBody>
        </Table>
      </Card>

      {selectedRun && (
        <Card p="4" mt="2" maxWidth="900px">
          <h3 className="mb-3 text-lg font-semibold border-b pb-2">
            Payslip — {selectedRun.periodStart} – {selectedRun.periodEnd}
          </h3>
          <Table>
            <TableHeader>
              <TableRow>
                <TableCell className="text-left py-2">Employee</TableCell>
                <TableCell className="text-left py-2">Gross</TableCell>
                <TableCell className="text-left py-2">Deductions</TableCell>
                <TableCell className="text-left py-2">Net</TableCell>
                <TableCell className="text-left py-2">Reference</TableCell>
              </TableRow>
            </TableHeader>
            <TableBody>
              {items.map((it) => (
                <TableRow key={it.employeeName} className="border-b">
                  <TableCell className="py-3">{it.employeeName}</TableCell>
                  <TableCell className="py-3">{it.grossAmount} {it.currency}</TableCell>
                  <TableCell className="py-3">{it.deductionsAmount} {it.currency}</TableCell>
                  <TableCell className="py-3">{it.netAmount} {it.currency}</TableCell>
                  <TableCell className="py-3">{it.calculationReference}</TableCell>
                </TableRow>
              ))}
              {items.length === 0 && (
                <TableRow>
                  <TableCell colSpan={5} className="py-3 text-center text-muted-inline">
                    No line items for this run.
                  </TableCell>
                </TableRow>
              )}
            </TableBody>
          </Table>

          <div className="mt-4">
            <Button onClick={() => window.print()} className="mr-2">
              View/Download Payslip (Print)
            </Button>
            <Button variant="ghost" onClick={() => setSelectedRun(null)}>
              Close
            </Button>
          </div>
        </Card>
      )}

      {role !== "EMPLOYEE" && (
        <Button onClick={() => alert("Implement add-run form")} className="mb-4">
          Initiate New Payroll Run
        </Button>
      )}
    </Card>
  );
};

export default PayrollManagementPage;