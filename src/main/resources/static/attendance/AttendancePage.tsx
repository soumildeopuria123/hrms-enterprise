import React, { useEffect, useState } from "react";
import {
  Card,
  Button,
  Badge,
  Table,
  TableHeader,
  TableRow,
  TableCell,
  Input,
  Label,
} from "@/components/neo-brutalist"; // adjust import path
import axios from "axios";
import { useAuth } from "@/hooks/useAuth";

interface AttendanceRecord {
  id: number;
  employeeName: string;
  workDate: string;
  clockInAt?: string;
  clockOutAt?: string;
  status: string;
}

const AttendancePage: React.FC = () => {
  const { token, employeeId, role } = useAuth();
  const [records, setRecords] = useState<AttendanceRecord[]>([]);
  const [open, setOpen] = useState(false);

  useEffect(() => {
    fetchAttendanceLogs();
  }, [token, employeeId, role]);

  const fetchAttendanceLogs = async () => {
    try {
      let url = "/api/attendance";
      if (role === "EMPLOYEE" && employeeId) {
        url += `?employeeId=${employeeId}`;
      }
      const res = await axios.get(url, {
        headers: { Authorization: `Bearer ${token}` },
      });
      setRecords(res.data);
    } catch (err) {
      console.error("Failed to fetch attendance logs", err);
    }
  };

  const handleOpen = () => setOpen(true);
  const handleClose = () => setOpen(false);

  const handleClockIn = async () => {
    try {
      await axios.post("/api/attendance/clock-in", { source: "web" }, {
        headers: { Authorization: `Bearer ${token}` },
      });
      fetchAttendanceLogs();
    } catch (err) {
      console.error("Clock‑in failed", err);
    }
    setOpen(false);
  };

  const handleClockOut = async () => {
    try {
      await axios.post("/api/attendance/clock-out", { source: "web" }, {
        headers: { Authorization: `Bearer ${token}` },
      });
      fetchAttendanceLogs();
    } catch (err) {
      console.error("Clock‑out failed", err);
    }
    setOpen(false);
  };

  const statusBadgeClass = (status: string) => {
    switch (status.toLowerCase()) {
      case "present":
        return "bg-green-100 text-green-800 border-green-200";
      case "late":
        return "bg-yellow-100 text-yellow-800 border-yellow-200";
      case "absent":
        return "bg-red-100 text-red-800 border-red-200";
      default:
        return "bg-gray-100 text-gray-800 border-gray-200";
    }
  };

  return (
    <Card p="6" maxWidth="800px">
      <h2 className="mb-4 text-2xl font-bold border-b pb-2">Attendance Tracking</h2>

      <div className="mb-4">
        {role === "EMPLOYEE" && employeeId ? (
          <>
            <Button onClick={() => setOpen(true)} className="mb-2">
              {open ? "Clock Out" : "Clock In"}
            </Button>
            <Button variant="ghost" onClick={() => setOpen(false)} className="mt-2">
              ×
            </Button>
          </>
        ) : null}
      </div>

      <Card p="4" mt="2" maxWidth="100%">
        <Table>
          <TableHeader>
            <TableRow>
              <TableCell className="text-left py-2">Date</TableCell>
              <TableCell className="text-left py-2">Employee</TableCell>
              <TableCell className="text-left py-2">Clock In</TableCell>
              <TableCell className="text-left py-2">Clock Out</TableCell>
              <TableCell className="text-left py-2">Status</TableCell>
            </TableRow>
          </TableHeader>
          <TableBody>
            {records.map((rec) => (
              <TableRow key={rec.id} className="border-b">
                <TableCell className="py-3">{rec.workDate}</TableCell>
                <TableCell className="py-3">{rec.employeeName}</TableCell>
                <TableCell className="py-3">{rec.clockInAt || "—"}</TableCell>
                <TableCell className="py-3">{rec.clockOutAt || "—"}</TableCell>
                <TableCell className="py-3">
                  <Badge className={statusBadgeClass(rec.status)}>{rec.status}</Badge>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </Card>
    </Card>
  );
};

export default AttendancePage;