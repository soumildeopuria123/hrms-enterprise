import React, { useEffect, useState } from "react";
import {
  Card,
  Button,
  Badge,
  Input,
  Label,
  Select,
  Option,
  Textarea,
  Modal,
} from "@/components/neo-brutalist"; // adjust import path
import axios from "axios";
import { useAuth } from "@/hooks/useAuth";

interface ExpenseClaim {
  id: number;
  employeeName: string;
  expenseClaimId: string;
  amount: number;
  currency: string;
  expenseDate: string;
  category: string;
  businessPurpose: string;
  status: string;
}

const statusBadgeClasses: Record<string, string> = {
  Draft: "bg-gray-100 text-gray-800 border-gray-200",
  Submitted: "bg-yellow-100 text-yellow-800 border-yellow-200",
  "Pending Manager": "bg-yellow-100 text-yellow-800 border-yellow-200",
  "Pending Finance": "bg-yellow-100 text-yellow-800 border-yellow-200",
  Approved: "bg-green-100 text-green-800 border-green-200",
  Rejected: "bg-red-100 text-red-800 border-red-200",
};

const ExpenseClaimsPage: React.FC = () => {
  const { token, employeeId, role } = useAuth();
  const [claims, setClaims] = useState<ExpenseClaim[]>([]);
  const [open, setOpen] = useState(false);
  const [form, setForm] = useState<{
    amount: number;
    currency: string;
    expenseDate: string;
    category: string;
    businessPurpose: string;
  }>({
    amount: 0,
    currency: "USD",
    expenseDate: new Date().toISOString().split("T")[0],
    category: "Travel",
    businessPurpose: "",
  });

  useEffect(() => {
    fetchClaims();
  }, [token, employeeId, role]);

  const fetchClaims = async () => {
    try {
      let url = "/api/expenses";
      if (role === "EMPLOYEE" && employeeId) {
        url += `?employeeId=${employeeId}`;
      } else if (role === "MANAGER") {
        url += "?status=PENDING_MANAGER";
      } else if (role === "FINANCE") {
        url += "?status=PENDING_FINANCE";
      }
      const res = await axios.get(url, {
        headers: { Authorization: `Bearer ${token}` },
      });
      setClaims(res.data);
    } catch (err) {
      console.error("Failed to fetch expense claims", err);
    }
  };

  const handleOpen = () => setOpen(true);
  const handleClose = () => {
    setOpen(false);
    setForm({
      amount: 0,
      currency: "USD",
      expenseDate: new Date().toISOString().split("T")[0],
      category: "Travel",
      businessPurpose: "",
    });
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await axios.post("/api/expenses", {
        amount: form.amount,
        currency: form.currency,
        expenseDate: form.expenseDate,
        category: form.category,
        businessPurpose: form.businessPurpose,
      }, {
        headers: { Authorization: `Bearer ${token}` },
      });
      handleClose();
      fetchClaims();
    } catch (err) {
      console.error("Failed to submit expense claim", err);
    }
  };

  const handleStatusChange = async (claimId: number, newStatus: string) => {
    try {
      await axios.patch(`/api/expenses/${claimId}/status`, {
        decision: newStatus.toUpperCase(),
      }, {
        headers: { Authorization: `Bearer ${token}` },
      });
      fetchClaims();
    } catch (err) {
      console.error("Failed to update claim status", err);
    }
    setOpen(false);
  };

  return (
    <Card p="6" maxWidth="900px">
      <h2 className="mb-4 text-2xl font-bold border-b pb-2">Expense Claims</h2>

      <Button onClick={handleOpen} className="mb-4">
        Submit Expense Claim
      </Button>

      <Modal open={open} onClose={handleClose} className="mx-auto">
        <Modal.Header className="pb-3 border-b">
          <h3 className="text-xl font-semibold">New Expense Claim</h3>
          <Button variant="ghost" onClose={handleClose}>×</Button>
        </Modal.Header>
        <Modal.Body className="space-y-4">
          <form onSubmit={handleSubmit} className="space-y-3">
            <Label>Amount</Label>
            <Input
              type="number"
              step="0.01"
              value={form.amount}
              onChange={(e) =>
                setForm({ ...form, amount: Number(e.target.value) })
              }
              placeholder="0.00"
              className="w-full"
            />

            <Label>Currency</Label>
            <Select
              value={form.currency}
              onChange={(e) => setForm({ ...form, currency: e.target.value })}
              className="w-full"
            >
              <Option value="USD">USD</Option>
              <Option value="EUR">EUR</Option>
              <Option value="GBP">GBP</Option>
            </Select>

            <Label>Expense Date</Label>
            <Input
              type="date"
              value={form.expenseDate}
              onChange={(e) =>
                setForm({ ...form, expenseDate: e.target.value })
              }
              className="w-full"
            />

            <Label>Category</Label>
            <Select
              value={form.category}
              onChange={(e) => setForm({ ...form, category: e.target.value })}
              className="w-full"
            >
              <Option value="Travel">Travel</Option>
              <Option value="Meals">Meals</Option>
              <Option value="Software">Software</Option>
              <Option value="Equipment">Equipment</Option>
            </Select>

            <Label>Business Purpose</Label>
            <Textarea
              value={form.businessPurpose}
              onChange={(e) =>
                setForm({ ...form, businessPurpose: e.target.value })
              }
              placeholder="Brief description of the business purpose"
              className="w-full h-24"
            />

            <Button type="submit" className="mt-3">
              Submit Claim
            </Button>
          </form>
        </Modal.Body>
      </Modal>

      <Card p="4" mt="2" maxWidth="100%">
        <Table>
          <TableHeader>
            <TableRow>
              <TableCell className="text-left py-2">Date</TableCell>
              <TableCell className="text-left py-2">Employee</TableCell>
              <TableCell className="text-left py-2">Amount</TableCell>
              <TableCell className="text-left py-2">Currency</TableCell>
              <TableCell className="text-left py-2">Category</TableCell>
              <TableCell className="text-left py-2">Purpose</TableCell>
              <TableCell className="text-left py-2">Status</TableCell>
            </TableRow>
          </TableHeader>
          <TableBody>
            {claims.map((c) => (
              <TableRow key={c.id} className="border-b">
                <TableCell className="py-3">{c.expenseDate}</TableCell>
                <TableCell className="py-3">{c.employeeName}</TableCell>
                <TableCell className="py-3">{c.amount}</TableCell>
                <TableCell className="py-3">{c.currency}</TableCell>
                <TableCell className="py-3">{c.category}</TableCell>
                <TableCell className="py-3">{c.businessPurpose || "-"}<TableCell>
                <TableCell className="py-3">
                  <Badge className={statusBadgeClasses[c.status]}>
                    {c.status}
                  </Badge>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </Card>
    </Card>
  );
};

export default ExpenseClaimsPage;