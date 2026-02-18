import React, { useState } from "react";
import { Filter, LogOut, ChevronDown } from "lucide-react";

const Navbar = ({ interviewer, onOpenFilters }) => {
  const [isProfileOpen, setIsProfileOpen] = useState(false);

  // 🔥 SAFE ACCESS
  const fullName = interviewer?.fullName || "Interviewer";
  const email = interviewer?.email || "";
  const initials = fullName
    .split(" ")
    .map((n) => n[0])
    .join("")
    .substring(0, 2)
    .toUpperCase();

  return (
    <div className="h-16 bg-[#101828] text-white flex items-center justify-between px-8 shrink-0 shadow-lg z-50">
      
      {/* LEFT LOGO */}
      <div className="flex items-center gap-3">
        <div className="h-8 w-8 bg-[#007bff] rounded flex items-center justify-center font-bold">
          T
        </div>
        <span className="font-bold tracking-tight text-lg">
          TurboHire
        </span>
      </div>

      {/* RIGHT SIDE */}
      <div className="flex items-center gap-6">

        <button
          onClick={onOpenFilters}
          className="flex items-center gap-2 bg-white/10 hover:bg-white/20 px-4 py-2 rounded-xl transition-all font-bold text-sm"
        >
          <Filter size={16} /> Filters
        </button>

        <div className="relative">
          <button
            onClick={() => setIsProfileOpen(!isProfileOpen)}
            className="flex items-center gap-3 hover:bg-white/10 p-1.5 pr-3 rounded-2xl transition-all"
          >
            <div className="h-9 w-9 bg-[#007bff] rounded-xl flex items-center justify-center font-bold text-white shadow-md">
              {initials}
            </div>

            <div className="text-left hidden md:block">
              <p className="text-xs font-bold leading-none">
                {fullName}
              </p>
              <p className="text-[10px] text-gray-400 font-medium mt-1">
                Interviewer
              </p>
            </div>

            <ChevronDown
              size={14}
              className={`text-gray-400 transition-transform ${
                isProfileOpen ? "rotate-180" : ""
              }`}
            />
          </button>

          {isProfileOpen && (
            <div className="absolute top-full right-0 mt-3 w-56 bg-white rounded-2xl shadow-2xl border border-gray-100 py-2 text-[#101828]">
              
              <div className="px-4 py-3 border-b border-gray-50">
                <p className="text-sm font-bold truncate">
                  {email}
                </p>
              </div>

              <button
                onClick={() => {
                  localStorage.removeItem("token");
                  window.location.href = "/";
                }}
                className="w-full flex items-center gap-3 px-4 py-3 text-sm font-bold text-red-500 hover:bg-red-50 transition-colors"
              >
                <LogOut size={16} /> Sign Out
              </button>

            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default Navbar;
