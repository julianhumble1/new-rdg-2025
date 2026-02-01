const Policies = () => {
  const policyFiles = [
    "Child Protection & Safeguarding Policy January 2026.pdf",
    "Data Privacy Notice January 2026.pdf",
    "Data Privacy Policy January 2026.pdf",
  ];

  return (
    <div className="flex flex-col gap-2">
      {policyFiles.map((file) => {
        const label = file
          .replace(/[-_]/g, " ")
          .replace(/\.pdf$/i, "")
          .replace(/\b\w/g, (c) => c.toUpperCase());
        return (
          <div key={file}>
            <a
              href={`/policies/${file}`}
              download
              target="_blank"
              rel="noopener noreferrer"
              className="text-rdg-red underline font-bold text-sm"
            >
              {label}
            </a>
            <img
              src="/icons/pdf-svgrepo-com.svg"
              alt="pdf-download"
              className="h-5 inline ml-2"
            />
          </div>
        );
      })}
    </div>
  );
};

export default Policies;
