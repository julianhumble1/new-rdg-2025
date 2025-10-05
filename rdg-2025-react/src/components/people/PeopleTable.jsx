import { PlusCircleIcon } from "@heroicons/react/16/solid";
import { Table } from "flowbite-react";
import { Link } from "react-router-dom";
import PersonRow from "./PersonRow.jsx";

const PeopleTable = ({ people, responseType, handleDelete, nameSearch }) => {
  if (people.length > 0) {
    return (
      <Table hoverable className="border overflow-auto max-w-screen">
        {responseType === "DETAILED" ? (
          <Table.Head className="text-lg">
            <Table.HeadCell>Name</Table.HeadCell>
            <Table.HeadCell>Home Phone</Table.HeadCell>
            <Table.HeadCell>Mobile Phone</Table.HeadCell>
            <Table.HeadCell>Address</Table.HeadCell>
            <Table.HeadCell>Street</Table.HeadCell>
            <Table.HeadCell>Postcode</Table.HeadCell>
            <Table.HeadCell>Actions</Table.HeadCell>
          </Table.Head>
        ) : (
          <Table.Head className="text-lg">
            <Table.HeadCell>Name</Table.HeadCell>
            <Table.HeadCell>Summary</Table.HeadCell>
          </Table.Head>
        )}
        <Table.Body className="divide-y">
          {responseType === "DETAILED" && (
            <Table.Row className="">
              <Table.Cell className="whitespace-nowrap font-medium text-gray-900 dark:text-white ">
                <Link
                  className="hover:underline flex flex-row gap-2"
                  to="/people/new"
                >
                  <PlusCircleIcon className="h-5 text-black text-opacity-75" />
                  <div className="my-auto">Add New Person</div>
                </Link>
              </Table.Cell>
            </Table.Row>
          )}
          {people.map((person, index) => (
            <PersonRow
              key={index}
              responseType={responseType}
              person={person}
              handleDelete={handleDelete}
              nameSearch={nameSearch}
            />
          ))}
        </Table.Body>
      </Table>
    );
  } else {
    return <div>No people to display</div>;
  }
};

export default PeopleTable;
