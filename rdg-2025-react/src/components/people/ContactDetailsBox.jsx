import { format } from "date-fns";
import AddressHelper from "../../utils/AddressHelper.js";

const ContactDetailsBox = ({ personData }) => {
  const fullAddress = AddressHelper.getFullAddress(
    personData.addressStreet,
    personData.addressTown,
    personData.addressPostcode,
  );

  return (
    <div className="flex flex-col text-sm p-2 gap-1 sm:text-start text-center">
      {fullAddress && (
        <div>
          <div className="font-bold italic">Address</div>
          <div>{fullAddress}</div>
        </div>
      )}
      {personData.homePhone && (
        <div className="flex flex-col">
          <div className="font-bold italic ">Home</div>
          <div>{personData.homePhone}</div>
        </div>
      )}
      {personData.mobilePhone && (
        <div className="flex flex-col">
          <div className="font-bold italic">Mobile</div>
          <div>{personData.mobilePhone}</div>
        </div>
      )}
      <div>
        <div className="flex text-sm gap-1">
          <div className="font-bold italic">Created:</div>
          <div>{format(new Date(personData.createdAt), "dd-MM-yyyy")}</div>
        </div>
        <div className="flex text-sm gap-1">
          <div className="font-bold italic">Last Updated:</div>
          <div>{format(new Date(personData.updatedAt), "dd-MM-yyyy")}</div>
        </div>
      </div>
    </div>
  );
};

export default ContactDetailsBox;
