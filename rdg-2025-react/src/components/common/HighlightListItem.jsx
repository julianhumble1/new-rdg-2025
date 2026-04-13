import { Link } from "react-router-dom";

const HighlightListItem = ({ label, value, link, linkType }) => {
  if (!value) return null;

  return (
    <div>
      <div className="font-bold italic">{label}</div>

      {link ? (
        linkType === "external" ? (
          <a
            href={link}
            className="text-blue-500 hover:text-blue-700 hover:underline"
            target="_blank"
          >
            {value}
          </a>
        ) : (
          <Link className="hover:underline font-bold w-fit" to={link}>
            {value}
          </Link>
        )
      ) : (
        <div className="text-wrap">{value}</div>
      )}
    </div>
  );
};

export default HighlightListItem;