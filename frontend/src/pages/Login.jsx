import React, { useState } from "react";
import { Eye, EyeOff, Activity, BarChart3, PieChart } from "lucide-react";
import { loginApi } from "../api/auth.api";
import { useAuth } from "../auth/AuthContext";
import { useNavigate } from "react-router-dom";

const LoginPage = () => {
  const { login } = useAuth();
  const navigate = useNavigate();

  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [showPassword, setShowPassword] = useState(false);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");

    try {
      setLoading(true);

      // 🔑 LOGIN API
      const token = await loginApi(email, password);

      // 🔑 STORE TOKEN
      login(token);

      // 🔑 DECODE ROLE
      const payload = JSON.parse(atob(token.split(".")[1]));
      const role = payload.role;

      // 🔑 ROUTE BASED ON ROLE
      if (role === "ADMIN") navigate("/admin");
      else if (role === "RECRUITER") navigate("/recruiter/jobs");
      else if (role === "USER") navigate("/interviewer");
      else navigate("/login");
    } catch (err) {
      console.error(err);
      setError("Invalid email or password");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="h-screen w-screen flex items-center justify-center bg-linear-to-br from-[#101828] via-[#0b0f1a] to-black font-['Montserrat'] p-4 overflow-hidden">
      <div className="max-w-6xl w-full max-h-[90vh] flex bg-white/10 backdrop-blur-xl rounded-[32px] overflow-hidden border border-white/20 shadow-2xl shadow-black/50">
        {/* LEFT SECTION */}
        <div className="hidden lg:flex lg:w-1/2 p-12 flex-col text-white">
          <div className="flex items-center gap-4">
          <div className="w-10 h-10 bg-black rounded-md flex items-center justify-center relative overflow-hidden shadow-sm group-hover:shadow-md transition-all">
            <div className="absolute inset-0 bg-gray-900 group-hover:bg-black transition-colors"></div>
            <span className="relative z-10 text-[#008AFF] font-bold text-xl mr-[1px]">
              T
            </span>
            <span className="relative z-10 text-white font-bold text-xl">
              H
            </span>
          </div>
            <span className="text-2xl font-bold">TurboHire</span>
          </div>

          <div className="space-y-4 mt-12">
            <h2 className="text-4xl font-extrabold">
              Hire <span className="text-blue-400">smarter</span> with advanced
              analytics.
            </h2>
            <p className="text-lg text-gray-300 max-w-md">
              Leverage our powerful analytics dashboard to identify bottlenecks
              and optimize your recruitment funnel.
            </p>

            <div className="mt-8 grid grid-cols-2 gap-4 opacity-80">
              <div className="bg-white/10 p-5 rounded-2xl">
                <BarChart3 className="text-blue-400 mb-3" size={28} />
                <div className="h-2 w-full bg-white/20 rounded-full mb-2"></div>
                <div className="h-2 w-2/3 bg-white/20 rounded-full"></div>
              </div>
              <div className="bg-white/10 p-5 rounded-2xl">
                <PieChart className="text-emerald-400 mb-3" size={28} />
                <div className="h-2 w-full bg-white/20 rounded-full mb-2"></div>
                <div className="h-2 w-1/2 bg-white/20 rounded-full"></div>
              </div>
            </div>
          </div>
        </div>

        {/* RIGHT SECTION */}
        <div className="w-full lg:w-1/2 bg-white/95 p-10 lg:p-16 flex flex-col justify-center">
          <div className="max-w-sm mx-auto w-full">
            <h3 className="text-3xl font-bold mb-1">Welcome To Turbohire</h3>
            <p className="text-gray-500 mb-6">
              The <span className="text-blue-400">Nexus</span> where talent
              meets opportunity
            </p>

            {error && <p className="text-red-500 text-sm mb-4">{error}</p>}

            <form onSubmit={handleSubmit} className="space-y-5">
              <div>
                <label className="text-xs font-bold text-gray-400 uppercase ml-1">
                  Work Email
                </label>
                <input
                  type="email"
                  required
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  className="w-full px-5 py-3 rounded-2xl border focus:ring-4 focus:ring-blue-500/10"
                  placeholder="name@company.com"
                />
              </div>

              <div>
                <label className="text-xs font-bold text-gray-400 uppercase ml-1">
                  Password
                </label>
                <div className="relative">
                  <input
                    type={showPassword ? "text" : "password"}
                    required
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    className="w-full px-5 py-3 rounded-2xl border focus:ring-4 focus:ring-blue-500/10"
                    placeholder="••••••••"
                  />
                  <button
                    type="button"
                    onClick={() => setShowPassword(!showPassword)}
                    className="absolute right-5 top-1/2 -translate-y-1/2 text-gray-400"
                  >
                    {showPassword ? <EyeOff /> : <Eye />}
                  </button>
                </div>
              </div>

              <button
                type="submit"
                disabled={loading}
                className="w-full bg-[#101828] text-white py-4 rounded-2xl flex justify-center gap-2 cursor-pointer"
              >
                {loading ? "Signing In..." : "Sign In"}
                <Activity size={18} />
              </button>
            </form>
          </div>
        </div>
      </div>
    </div>
  );
};

export default LoginPage;
