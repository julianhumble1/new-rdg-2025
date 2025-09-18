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
      <div></div>
    </div>
  );
};

export default ContactDetailsBox;
