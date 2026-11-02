export interface TenantUser {
  sub: string;
  employeeId: number;
  role: "EMPLOYEE" | "MANAGER" | "FINANCE" | "HR" | "ADMIN";
  tenantId: string;
}

/**
 * useAuth reads the current session from a JWT in localStorage.
 * Replace the storage key and token shape with your security setup.
 */
export function useAuth() {
  const [user, setUser] = React.useState<TenantUser | null>(null);
  const [token, setToken] = React.useState<string | null>(null);

  React.useEffect(() => {
    const stored = localStorage.getItem("hrms_token");
    if (stored) {
      try {
        const decoded = JSON.parse(atob(stored.split(".")[1] || ""));
        setUser(decoded as TenantUser);
        setToken(stored);
      } catch {
        localStorage.removeItem("hrms_token");
      }
    }
  }, []);

  return { token, employeeId: user?.employeeId ?? null, role: user?.role ?? "EMPLOYEE", tenantId: user?.tenantId ?? "" };
}
