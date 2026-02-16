import { NavLink } from "react-router-dom";

const menu = [
  { label: "Dashboard", path: "/recruiter" },
  { label: "Business Units", path: "/recruiter/business-units" },
  { label: "Jobs", path: "/recruiter/jobs" },
  { label: "Candidates", path: "/recruiter/candidates" },
  { label: "Pipeline", path: "/recruiter/pipeline" },
  { label: "Interviews", path: "/recruiter/interviews" },
  { label: "Offers", path: "/recruiter/offers" },
];

export default function Sidebar() {
  return (
    <aside className="w-64 bg-gray-900 text-white min-h-screen">
      <div className="p-6 text-xl font-bold border-b border-gray-700">
        TurboHire
      </div>

      <nav className="p-4 space-y-2">
        {menu.map((item) => (
          <NavLink
            key={item.path}
            to={item.path}
            end={item.path === "/recruiter"}
            className={({ isActive }) =>
              `block px-4 py-2 rounded ${
                isActive ? "bg-blue-600" : "hover:bg-gray-700"
              }`
            }
          >
            {item.label}
          </NavLink>
        ))}
      </nav>
    </aside>
  );
}
