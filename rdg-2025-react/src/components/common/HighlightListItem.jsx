import { Link } from "react-router-dom";

const HighlightListItem = (label, value, link) => {
    if (!value) return null;

    return (
        <div className="flex flex-col">
            <div className="font-bold italic">{label}</div>
            {link ? 
                <Link className="hover:underline font-bold" to={link}>{value}</Link>
                :
                <div>{value}</div>
            }
        </div>
    )
}

export default HighlightListItem