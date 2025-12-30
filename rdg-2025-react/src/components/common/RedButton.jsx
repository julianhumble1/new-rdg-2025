const RedButton = ({ onClick, children, altColor = "red" }) => {
  const determineColor = (color) => {
    switch (color) {
      case "red":
        return "bg-rdg-red";
      case "blue":
        return "bg-rdg-blue";
      case "yellow":
        return "bg-rdg-yellow";
    }
  };

  return (
    <button
      className={`${determineColor(altColor)} w-fit p-2 rounded-full font-bold px-6 text-white hover:scale-105 transition`}
      onClick={onClick}
    >
      {children}
    </button>
  );
};

export default RedButton;
