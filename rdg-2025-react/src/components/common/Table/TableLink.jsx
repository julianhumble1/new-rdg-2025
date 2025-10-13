const TableLink = ({ text, link }) => {
  return (
    <a href={link} className="font-bold hover:underline">
      {text}
    </a>
  );
};

export default TableLink;
