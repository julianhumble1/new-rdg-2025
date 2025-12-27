const RedButton = ({ onClick, children, altColor = "red" }) => {
  return (
    <button
      className={`bg-rdg-${altColor} w-fit p-2 rounded-full font-bold px-6 text-white hover:scale-105 transition`}
      onClick={onClick}
    >
      {children}
    </button>
  );
};

export default RedButton;
