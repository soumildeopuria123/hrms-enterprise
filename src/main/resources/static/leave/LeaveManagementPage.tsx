import React, { useEffect, useState } from "react";
import {
  Card,
  Button,
  Badge,
  Input,
  DatePicker,
  Modal,
  Form,
  Label,
} from "@/components/neo-brutalist"; // adjust import path
import axios from "axios";
import { useAuth } from "@/hooks/useAuth";

interface LeaveRequest {
  id: number;
  employeeName: string;
  leaveType: string;
  startDate: string;
  endDate: string;
  status: string;
  reason?: string;
}

const LeaveManagementPage: React.FC = () => {
  const { token, employeeId, role } = useAuth();
  const [requests, setRequests] = useState<LeaveRequest[]>([]);
  const [open, setOpen] = useState(false);
  const [formData, setFormData] = useState<{
    leaveType: string;
    startDate: string;
    endDate: string;
    reason: string;
  }>({
    leaveType: "",
    startDate: new Date().toISOString().split("T")[0],
    endDate: new Date().toISOString().split("T")[0],
    reason: "",
  });

  useEffect(() => {
    fetchLeaveRequests();
  }, [token, employeeId, role]);

  const fetchLeaveRequests = async () => {
    try {
      let url = "/api/leaves";
      if (role === "EMPLOYEE" && employeeId) {
        url += `?employeeId=${employeeId}`;
      }
      const res = await axios.get(url, {
        headers: { Authorization: `Bearer ${token}` },
      });
      setRequests(res.data);
    } catch (err) {
      console.error("Failed to fetch leave requests", err);
    }
  };

  const handleOpen = () => setOpen(true);
  const handleClose = () => {
    setOpen(false);
    setFormData({
      leaveType: "",
      startDate: new Date().toISOString().split("T")[0],
      endDate: new Date().toISOString().split("T")[0],
      reason: "",
    });
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await axios.post("/api/leaves", formData, {
        headers: { Authorization: `Bearer ${token}` },
      });
      handleClose();
      fetchLeaveRequests();
    } catch (err) {
      console.error("Failed to submit leave request", err);
    }
  };

  const statusBadgeClass = (status: string) => {
    switch (status.toLowerCase()) {
      case "pending":
        return "bg-yellow-100 text-yellow-800 border-yellow-200";
      case "approved":
        return "bg-green-100 text-green-800 border-green-200";
      case "rejected":
        return "bg-red-100 text-red-800 border-red-200";
      default:
        return "bg-gray-100 text-gray-800 border-gray-200";
    }
  };

  return (
    <Card p="6" maxWidth="600px">
      <h2 className="mb-4 text-2xl font-bold border-b pb-2">Leave Management</h2>

      <Button onClick={handleOpen} className="mb-4">
        Submit Leave Request
      </Button>

      <Modal open={open} onClose={handleClose} className="mx-auto">
        <Modal.Header className="pb-3 border-b">
          <h3 className="text-xl font-semibold">New Leave Request</h3>
          <Button variant="ghost" onClose={handleClose}>×</Button>
        </Modal.Header>
        <Modal.Body className="space-y-4">
          <Form onSubmit={handleSubmit} className="space-y-3">
            <Label>Leave Type</Label>
            <Input
              type="text"
              value={formData.leaveType}
              onChange={(e) =>
                setFormData({ ...formData, leaveType: e.target.value })
              }
              placeholder="e.g., Annual, Sick"
              className="w-full"
            />

            <Label>Start Date</Label>
            <DatePicker
              value={formData.startDate}
              onChange={(date) =>
                setFormData({ ...formData, startDate: date.toISOString().split("T")[0] })
              }
            />

            <Label>End Date</Label>
            <DatePicker
              value={formData.endDate}
              onChange={(date) =>
                setFormData({ ...formData, endDate: date.toISOString().split("T")[0] })
              }
            />

            <Label>Reason</Label>
            <Input
              type="text"
              value={formData.reason}
              onChange={(e) =>
                setFormData({ ...formData, reason: e.target.value })
              }
              placeholder="Brief reason for leave"
              className="w-full"
            />

            <Button type="submit" className="mt-3">
              Submit Request
            </Button>
          </Form>
        </Modal.Body>
      </Modal>

      <Card p="6" mt="2" maxWidth="800px">
        <h3 className="mb-4 text-xl font-bold border-b pb-2">My Leave Requests</h3>
        {requests.length === 0 && <p className="text-muted-inline">No leave requests found.</p>}
        <table className="w-full border-collapse">
          <thead>
            <tr>
              <th className="text-left py-3 border-b">Period</th>
              <th className="text-left py-3 border-b">Type</th>
              <th className="text-left py-3 border-b">Status</th>
              <th className="text-left py-3 border-b">Reason</th>
            </tr>
          </thead>
          <tbody>
            {requests.map((req) => (
              <tr key={req.id} className="border-b">
                <td className="py-3">{req.startDate} – {req.endDate}</td>
                <td className="py-3">{req.leaveType}</td>
                <td className="py-3">
                  <Badge className={statusBadgeClass(req.status)}>{req.status}</Badge>
                </td>
                <td className="py-3">{req.reason || "-"}<td>
              </tr>
            ))}
          </tbody>
        </table>
      </Card>
    </Card>
  );
};

export default LeaveManagementPage;