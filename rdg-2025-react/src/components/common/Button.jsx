const Button = ({ children, onClick }) => {
  return <button onClick={onClick} className="bg-sky-900 rounded w-full mx-auto text-sm hover:font-bold py-2 shadow text-white">{children}</button>;
};

export default Button;
