import React from "react";
import { BrowserRouter, Routes, Route, NavLink } from "react-router-dom";
import LeaveManagementPage from "./pages/leave/LeaveManagementPage";
import AttendancePage from "./pages/attendance/AttendancePage";
import ExpenseClaimsPage from "./pages/expenses/ExpenseClaimsPage";
import PayrollManagementPage from "./pages/payroll/PayrollManagementPage";

const Nav = () => (
  <nav style={{ borderBottom: "2px solid #000", padding: "8px 16px" }}>
    <div style={{ display: "flex", gap: 16, fontWeight: 700 }}>
      <NavLink to="/" end style={({ isActive }) =>
        isActive ? { color: "#000", textDecoration: "underline", textUnderlinePosition: "under" } : { color: "#000" }
      }>Leave</NavLink>
      <NavLink to="/attendance" style={({ isActive }) =>
        isActive ? { color: "#000", textDecoration: "underline", textUnderlinePosition: "under" } : { color: "#000" }
      }>Attendance</NavLink>
      <NavLink to="/expenses" style={({ isActive }) =>
        isActive ? { color: "#000", textDecoration: "underline", textUnderlinePosition: "under" } : { color: "#000" }
      }>Expenses</NavLink>
      <NavLink to="/payroll" style={({ isActive }) =>
        isActive ? { color: "#000", textDecoration: "underline", textUnderlinePosition: "under" } : { color: "#000" }
      }>Payroll</NavLink>
    </div>
  </nav>
);

export default function App() {
  return (
    <BrowserRouter>
      <Nav />
      <main style={{ padding: 16 }}>
        <Routes>
          <Route path="/" element={<LeaveManagementPage />} />
          <Route path="/attendance" element={<AttendancePage />} />
          <Route path="/expenses" element={<ExpenseClaimsPage />} />
          <Route path="/payroll" element={<PayrollManagementPage />} />
        </Routes>
      </main>
    </BrowserRouter>
  );
}
