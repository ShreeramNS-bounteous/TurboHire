import { useAuth } from "../auth/AuthContext";
import { useNavigate, NavLink } from "react-router-dom";

export default function Navbar() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate("/login");
  };

  const linkClass = ({ isActive }) =>
    `flex items-center gap-2 px-2 py-1 text-sm font-medium transition
     ${isActive ? "text-white" : "text-gray-300 hover:text-white"}`;

  return (
    <div className="bg-[#101828] h-12">
      <div className="max-w-7xl mx-auto px-6 h-full flex items-center justify-between">
        {/* LEFT */}
        <div className="flex items-center gap-6">
          {/* Logo */}
          <div className="flex items-center gap-2 text-white font-semibold">
            <div className="w-6 h-6 bg-white text-black text-xs flex items-center justify-center rounded">
              TH
            </div>
          </div>

          {/* Nav links */}
          <nav className="flex items-center gap-5">
            <NavLink to="/recruiter/jobs" className={linkClass}>
              Jobs
            </NavLink>

            <NavLink to="/recruiter/candidates" className={linkClass}>
              Candidates
            </NavLink>

            <NavLink to="/recruiter/interviews" className={linkClass}>
              Interview
            </NavLink>

            <NavLink to="/recruiter/pipeline" className={linkClass}>
              Onboard
            </NavLink>

            <NavLink to="/recruiter/reports" className={linkClass}>
              Reports
            </NavLink>
          </nav>
        </div>

        {/* RIGHT */}
        <div className="flex items-center gap-4 text-sm">
          <span className="text-gray-300">
            {user?.email}
          </span>

          <button
            onClick={handleLogout}
            className="text-gray-300 hover:text-red-400 transition"
          >
            Logout
          </button>
        </div>
      </div>
    </div>
  );
}
