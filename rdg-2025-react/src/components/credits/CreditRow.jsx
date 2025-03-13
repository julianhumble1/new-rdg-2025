import { Table } from "flowbite-react"
import { Link } from "react-router-dom"
import Cookies from "js-cookie"

const CreditRow = ({ credit }) => {

    const role = Cookies.get("role")

    return (
        <Table.Row>
            <Table.Cell >
                <div className="flex flex-col">
                    <div className="font-bold">
                        {credit.name}
                    </div>
                    <div>
                        {credit.summary}
                    </div>
                </div>
            </Table.Cell>
            <Table.Cell className="whitespace-nowrap font-medium text-gray-900 dark:text-white ">
                <Link to={`/people/${credit.person.id}`} className="hover:underline">
                    {(credit.person != null) ?
                        `${credit.person.firstName} ${credit.person.lastName}` : ""
                    }
                </Link>
            </Table.Cell>
            <Table.Cell className="whitespace-nowrap font-medium text-gray-900 dark:text-white ">
                <Link to={`/productions/${credit.production.id}`} className="hover:underline">
                    {credit.production.name}
                </Link>
            </Table.Cell>
            {role === "ROLE_ADMIN" && 
                <Table.Cell className="whitespace-nowrap font-medium text-gray-900 dark:text-white ">
                    <Link to={`/credits/edit/${credit.id}`} className="hover:underline">
                        Edit
                    </Link>
                </Table.Cell>
            }

        </Table.Row>
    )
}

export default CreditRow