const baseClasses = "bg-slate-200 rounded shadow-md p-4 w-full";

const Card = ({ children, className = "" }) => {
    return (
        <div className={`${baseClasses} ${className}`}>
            {children}
        </div>
    );
};

export default Card;


