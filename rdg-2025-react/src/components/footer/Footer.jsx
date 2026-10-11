import Socials from "../modals/Socials.jsx";

const Footer = () => {
  return (
    <footer className="w-full bg-slate-50 py-6 px-4 mt-8">
      <div className="max-w-5xl mx-auto flex flex-col sm:flex-row items-center justify-between gap-4 text-sm">
        <div className="flex flex-col items-center sm:items-start gap-1">
          <div className="flex gap-2">
            <span>📧 Email:</span>
            <a
              className="text-rdg-blue hover:underline"
              href="mailto:info@rdg.org"
            >
              info@rdg.org
            </a>
          </div>
          <a className="text-rdg-blue hover:underline" href="/about/legal">
            Legal & Governance
          </a>
        </div>
        <Socials />
      </div>
    </footer>
  );
};

export default Footer;
