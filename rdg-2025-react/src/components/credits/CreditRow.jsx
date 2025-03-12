import { Table } from "flowbite-react"
import { Link } from "react-router-dom"

const CreditRow = ({ credit }) => {
    return (
        <Table.Row>
            <Table.Cell >
                {credit.name}
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
            <Table.Cell >
                {credit.summary}
            </Table.Cell>
        </Table.Row>
    )
}

export default CreditRow