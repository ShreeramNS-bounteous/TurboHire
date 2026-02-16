import React from "react";
import { useNavigate } from "react-router-dom";
import {
  UserCircle,
  Search,
  Briefcase,
  Globe,
  Shield,
  Users,
  MapPin,
  Clock,
  Heart,
  Zap,
  Coffee,
  GraduationCap,
} from "lucide-react";

export default function LandingPage() {
  const navigate = useNavigate();

  const scrollToSection = (id) => {
    const element = document.getElementById(id);
    if (element) {
      element.scrollIntoView({ behavior: "smooth" });
    }
  };

  return (
    <div className="font-sans text-slate-900 bg-white overflow-x-hidden">
      {/* ================= FIXED HEADER ================= */}
      <header className="fixed top-0 w-full z-50 bg-white/95 backdrop-blur-sm border-b border-gray-100 h-20 flex items-center justify-between px-6 lg:px-12 transition-all duration-300">
        {/* 1. Logo Section */}
        <div
          className="flex items-center gap-3 cursor-pointer group"
          onClick={() => navigate("/")}
        >
          <div className="w-10 h-10 bg-black rounded-md flex items-center justify-center relative overflow-hidden shadow-sm group-hover:shadow-md transition-all">
            <div className="absolute inset-0 bg-gray-900 group-hover:bg-black transition-colors"></div>
            <span className="relative z-10 text-[#008AFF] font-bold text-xl mr-[1px]">
              T
            </span>
            <span className="relative z-10 text-white font-bold text-xl">
              H
            </span>
          </div>
          <div className="flex flex-col leading-none justify-center">
            <span className="text-xl font-bold tracking-tight text-slate-900 group-hover:text-[#008AFF] transition-colors">
              TurboHire
            </span>
            <span className="text-[10px] font-bold text-slate-500 uppercase tracking-widest mt-[2px] group-hover:text-slate-700 transition-colors">
              BXA Hiring Portal
            </span>
          </div>
        </div>

        {/* 2. Navigation Links (Updated) */}
        <nav className="hidden lg:flex items-center gap-8">
          <button
            onClick={() => scrollToSection("open-positions")}
            className="text-[14px] font-medium text-slate-600 hover:text-[#008AFF] transition-colors relative group"
          >
            Open Positions
            <span className="absolute -bottom-1 left-0 w-0 h-[2px] bg-[#008AFF] transition-all group-hover:w-full"></span>
          </button>
          <button
            onClick={() => scrollToSection("benefits")}
            className="text-[14px] font-medium text-slate-600 hover:text-[#008AFF] transition-colors relative group"
          >
            Company Benefits
            <span className="absolute -bottom-1 left-0 w-0 h-[2px] bg-[#008AFF] transition-all group-hover:w-full"></span>
          </button>
        </nav>

        {/* 3. Login Action */}
        <div className="flex items-center gap-4">
          <button
            onClick={() => navigate("/login")}
            className="flex items-center gap-2 bg-[#008AFF] hover:bg-blue-600 text-white px-6 py-2.5 rounded-lg font-semibold text-sm transition-all shadow-md hover:shadow-lg hover:-translate-y-0.5"
          >
            <UserCircle className="w-4 h-4" />
            Portal Login
          </button>
        </div>
      </header>

      {/* ================= HERO SECTION ================= */}
      <main className="pt-20">
        <section className="relative min-h-[85vh] bg-[#0B1221] overflow-hidden flex flex-col items-center justify-center text-center px-4 py-20">
          <div className="absolute top-0 left-0 w-full h-full overflow-hidden pointer-events-none">
            <div className="absolute top-[-10%] left-[-10%] w-[500px] h-[500px] bg-blue-600/20 rounded-full blur-[100px]"></div>
            <div className="absolute bottom-[-10%] right-[-10%] w-[500px] h-[500px] bg-purple-600/20 rounded-full blur-[100px]"></div>
            <div className="absolute inset-0 bg-[url('https://grainy-gradients.vercel.app/noise.svg')] opacity-20"></div>
          </div>

          <div className="relative z-10 max-w-4xl mx-auto space-y-8 mt-6">
            <span className="inline-flex items-center gap-2 px-3 py-1 rounded-full bg-blue-900/50 border border-blue-700 text-blue-200 text-xs font-bold uppercase tracking-wider backdrop-blur-md">
              <span className="w-2 h-2 rounded-full bg-blue-400 animate-pulse"></span>
              Internal Talent Ecosystem
            </span>

            <h1 className="text-4xl md:text-6xl font-medium text-white leading-[1.1] tracking-tight">
              Reimagining Talent Acquisition for <br />
              <span className="text-transparent bg-clip-text bg-gradient-to-r from-blue-400 to-purple-400 font-serif italic">
                Bounteous x Accolite
              </span>
            </h1>

            <p className="text-lg md:text-xl text-slate-300 leading-relaxed max-w-2xl mx-auto font-light">
              Welcome to your centralized hub for sourcing, screening, and
              hiring the best talent. Powered by TurboHire's Agentic AI.
            </p>

            <div className="pt-10 grid grid-cols-1 sm:grid-cols-3 gap-4 w-full max-w-3xl mx-auto">
              <StatCard label="Active Jobs" value="120+" icon={Briefcase} />
              <StatCard label="Candidates" value="1,450" icon={Users} />
              <StatCard
                label="Avg. Time to Hire"
                value="18 Days"
                icon={Search}
              />
            </div>
          </div>
        </section>

        {/* ================= OPEN POSITIONS SECTION ================= */}
        <section
          id="open-positions"
          className="py-24 bg-slate-50 relative z-20"
        >
          <div className="max-w-7xl mx-auto px-6">
            <div className="text-center mb-16">
              <h2 className="text-3xl font-bold text-slate-900 mb-4">
                Open Positions
              </h2>
              <p className="text-slate-500 max-w-2xl mx-auto">
                Explore current opportunities across our global teams. Referrals
                are highly encouraged.
              </p>
            </div>

            <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-6">
              <JobCard
                role="Senior Java Developer"
                dept="Engineering"
                loc="Bengaluru, India"
                type="Full-time"
                posted="2 days ago"
              />
              <JobCard
                role="UX/UI Designer"
                dept="Design"
                loc="Hyderabad, India"
                type="Full-time"
                posted="5 days ago"
              />
              <JobCard
                role="Product Manager"
                dept="Product"
                loc="Remote / Hybrid"
                type="Full-time"
                posted="1 week ago"
              />
              <JobCard
                role="Data Engineer"
                dept="Data Science"
                loc="Pune, India"
                type="Full-time"
                posted="3 days ago"
              />
              <JobCard
                role="DevOps Engineer"
                dept="Operations"
                loc="Gurugram, India"
                type="Full-time"
                posted="Just now"
              />
              <JobCard
                role="HR Generalist"
                dept="Human Resources"
                loc="Mumbai, India"
                type="Full-time"
                posted="2 weeks ago"
              />
            </div>

            <div className="mt-12 text-center">
              <button className="px-8 py-3 border border-[#008AFF] text-[#008AFF] font-bold rounded-lg hover:bg-blue-50 transition-colors">
                View All 120+ Jobs
              </button>
            </div>
          </div>
        </section>

        {/* ================= COMPANY BENEFITS SECTION ================= */}
        <section
          id="benefits"
          className="py-24 bg-white relative z-20 border-t border-slate-100"
        >
          <div className="max-w-7xl mx-auto px-6">
            <div className="text-center mb-16">
              <h2 className="text-3xl font-bold text-slate-900 mb-4">
                Life at Bounteous x Accolite
              </h2>
              <p className="text-slate-500 max-w-2xl mx-auto">
                We believe in taking care of our people. Our comprehensive
                benefits package is designed to support you at every stage of
                life.
              </p>
            </div>

            <div className="grid md:grid-cols-2 lg:grid-cols-4 gap-8">
              <BenefitCard
                icon={Heart}
                title="Comprehensive Health"
                desc="Premium medical, dental, and vision coverage for you and your dependents."
              />
              <BenefitCard
                icon={Zap}
                title="Performance Bonus"
                desc="Competitive annual bonuses and spot awards for exceptional performance."
              />
              <BenefitCard
                icon={Globe}
                title="Remote First"
                desc="Work from where you are most productive with our flexible hybrid policies."
              />
              <BenefitCard
                icon={GraduationCap}
                title="Learning Budget"
                desc="Annual stipend for courses, certifications, and conferences to keep you growing."
              />
              <BenefitCard
                icon={Coffee}
                title="Wellness Programs"
                desc="Mental health support, gym memberships, and regular wellness workshops."
              />
              <BenefitCard
                icon={Users}
                title="Family Support"
                desc="Generous parental leave and childcare support to help your family thrive."
              />
              <BenefitCard
                icon={Shield}
                title="Retirement Plans"
                desc="Robust 401(k) and PF matching to help you secure your financial future."
              />
              <BenefitCard
                icon={Clock}
                title="Flexible Hours"
                desc="Balance your work and personal life with truly flexible working hours."
              />
            </div>
          </div>
        </section>

        {/* === FOOTER === */}
        <footer className="bg-slate-900 text-white py-12 border-t border-slate-800">
          <div className="max-w-7xl mx-auto px-6 flex flex-col md:flex-row justify-between items-center gap-6">
            <div className="flex flex-col items-center md:items-start gap-2">
              <div className="flex items-center gap-2">
                <div className="w-6 h-6 bg-blue-600 rounded flex items-center justify-center text-[10px] font-bold">
                  BXA
                </div>
                <span className="font-bold text-lg">Bounteous x Accolite</span>
              </div>
              <p className="text-slate-400 text-sm">
                Powered by TurboHire Intelligent Recruitment
              </p>
            </div>

            <div className="flex gap-8 text-sm font-medium text-slate-400">
              <a href="#" className="hover:text-white transition-colors">
                Internal Policy
              </a>
              <a href="#" className="hover:text-white transition-colors">
                Help Desk
              </a>
              <a href="#" className="hover:text-white transition-colors">
                Privacy
              </a>
            </div>

            <p className="text-slate-500 text-sm">
              &copy; 2026 Bounteous x Accolite.
            </p>
          </div>
        </footer>
      </main>
    </div>
  );
}

// === HELPER COMPONENTS ===

function StatCard({ label, value, icon: Icon }) {
  return (
    <div className="bg-white/10 backdrop-blur-md border border-white/10 p-6 rounded-xl flex items-center gap-4 text-left hover:bg-white/15 transition-colors cursor-default">
      <div className="w-10 h-10 rounded-full bg-blue-500/20 flex items-center justify-center text-blue-300">
        <Icon className="w-5 h-5" />
      </div>
      <div>
        <div className="text-2xl font-bold text-white">{value}</div>
        <div className="text-xs font-medium text-slate-400 uppercase tracking-wider">
          {label}
        </div>
      </div>
    </div>
  );
}

function JobCard({ role, dept, loc, type, posted }) {
  return (
    <div className="bg-white p-6 rounded-xl border border-stone-200 shadow-sm hover:shadow-md hover:border-blue-200 transition-all cursor-pointer group">
      <div className="flex justify-between items-start mb-4">
        <div>
          <span className="text-xs font-bold text-blue-600 bg-blue-50 px-2 py-1 rounded mb-2 inline-block">
            {dept}
          </span>
          <h3 className="text-lg font-bold text-slate-900 group-hover:text-blue-600 transition-colors">
            {role}
          </h3>
        </div>
        <span className="text-xs text-slate-400 font-medium">{posted}</span>
      </div>

      <div className="flex flex-col gap-2 text-sm text-slate-500 mb-6">
        <div className="flex items-center gap-2">
          <MapPin className="w-4 h-4 text-slate-400" />
          {loc}
        </div>
        <div className="flex items-center gap-2">
          <Clock className="w-4 h-4 text-slate-400" />
          {type}
        </div>
      </div>

      <button className="w-full py-2 rounded-lg border border-slate-200 text-slate-600 font-bold text-sm group-hover:bg-blue-600 group-hover:text-white group-hover:border-blue-600 transition-all">
        View Details
      </button>
    </div>
  );
}

function BenefitCard({ icon: Icon, title, desc }) {
  return (
    <div className="p-6 rounded-xl bg-slate-50 border border-slate-100 hover:bg-white hover:shadow-lg hover:-translate-y-1 transition-all duration-300 group">
      <div className="w-12 h-12 bg-white rounded-lg flex items-center justify-center mb-4 text-slate-400 shadow-sm group-hover:bg-blue-600 group-hover:text-white transition-colors">
        <Icon className="w-6 h-6" />
      </div>
      <h3 className="text-lg font-bold text-slate-900 mb-2">{title}</h3>
      <p className="text-slate-500 text-sm leading-relaxed">{desc}</p>
    </div>
  );
}
