const ContentCard = ({ children }) => {
  return (
    <div className="flex justify-center">
      <div className="md:w-5/6 w-full bg-slate-300 p-3 md:rounded-xl shadow-xl md:m-3">
        {children}
      </div>
    </div>
  );
};

export default ContentCard;
