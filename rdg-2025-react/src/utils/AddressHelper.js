export default class AddressHelper {

    static getFullAddress = (address, town, postcode) => {
        if (address && (town || postcode)) {
            address = address + ", "
        }
        if (town && postcode) {
            town = town + ", "
        }
        return address + town + postcode
    }   


}