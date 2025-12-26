const RedButton = ({ onClick, children }) => {
  return (
    <button
      className="bg-rdg-red w-fit p-2 rounded-full font-bold px-6 text-white hover:scale-105 transition"
      onClick={onClick}
    >
      {children}
    </button>
  );
};

export default RedButton;
