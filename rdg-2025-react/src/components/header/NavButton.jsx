import clsx from "clsx"
import { useEffect, useState } from "react"
import { Link } from "react-router-dom"

const NavButton = ({ buttonName }) => {

    const url = window.location.pathname

    const [active, setActive] = useState(false)

    useEffect(() => {
        console.log(url)
        console.log(buttonName)
        if (url.includes(buttonName.toLowerCase())) {
            setActive(true)
        } else {
            setActive(false)
        }

    }, [url, buttonName])

    return (
        <Link to={`/${buttonName.toLowerCase()}`} className='group'>
            <div className={clsx({ "font-bold": active }, "text-white")}>{buttonName}</div>
            <div className='flex justify-center w-full'>
                <div className={clsx(
                    { "h-1": active }, { "opacity-50": !active }, "w-3", "group-hover:w-5", "group-hover:h-1", "duration-100", "bg-white", "rounded",
                )}></div>
            </div>
        </Link>
    )
}

export default NavButton