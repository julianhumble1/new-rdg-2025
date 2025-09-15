import { Table } from "flowbite-react";
import { Link } from "react-router-dom";

const AwardRow = ({ award, handleDelete }) => {
  return (
    <Table.Row className="bg-white dark:border-gray-700 dark:bg-gray-800">
      <Table.Cell className="whitespace-nowrap font-medium text-gray-900 dark:text-white">
        {award.name}
      </Table.Cell>
      <Table.Cell>
        {award.production ? (
          <Link
            to={`/productions/${award.production.id}`}
            className="hover:underline"
          >
            {award.production.name}
          </Link>
        ) : (
          ""
        )}
      </Table.Cell>
      <Table.Cell>
        {award.festival ? (
          <Link
            to={`/festivals/${award.festival.id}`}
            className="hover:underline"
          >
            {award.festival.name}
          </Link>
        ) : (
          ""
        )}
      </Table.Cell>
      <Table.Cell>
        {award.person ? (
          <Link to={`/people/${award.person.id}`} className="hover:underline">
            {award.person.firstName} {award.person.lastName}
          </Link>
        ) : (
          ""
        )}
      </Table.Cell>
      <Table.Cell>
        <div className="flex gap-2">
          <Link
            className="text-medium text-black hover:underline font-bold text-end"
            to={`/awards/edit/${award.id}`}
          >
            Edit
          </Link>
          <button
            className="text-medium text-black hover:underline font-bold text-end"
            onClick={() => handleDelete(award)}
          >
            Delete
          </button>
        </div>
      </Table.Cell>
    </Table.Row>
  );
};

export default AwardRow;
