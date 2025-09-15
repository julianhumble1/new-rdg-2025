import FestivalService from "../services/FestivalService.js";
import ProductionService from "../services/ProductionService.js";
import VenueService from "../services/VenueService.js";
import PersonService from "../services/PersonService.js";

export default class FetchValueOptionsHelper {
  static fetchProductionOptions = async () => {
    try {
      const response = await ProductionService.getAllProductions();
      const productionList = response.data.productions;
      return productionList.map((production) => ({
        value: production.id,
        label: production.name,
        venue: production.venue ? production.venue : "",
      }));
    } catch (e) {
      throw new Error(e.message, e);
    }
  };

  static fetchVenueOptions = async () => {
    try {
      const response = await VenueService.getAllVenues();
      const venueList = response.data.venues;
      return venueList.map((venue) => ({ value: venue.id, label: venue.name }));
    } catch (e) {
      throw new Error(e.message, e);
    }
  };

  static fetchFestivalOptions = async () => {
    try {
      const response = await FestivalService.getAllFestivals();
      const festivalList = response.data.festivals;
      return festivalList.map((festival) => ({
        value: festival.id,
        label: `${festival.name} (${festival.year})`,
      }));
    } catch (e) {
      throw new Error(e.message, e);
    }
  };

  static fetchPersonOptions = async () => {
    try {
      const response = await PersonService.getAllPeople();
      const personList = response.data.people;
      return personList.map((person) => ({
        value: person.id,
        label: `${person.firstName} ${person.lastName}`,
      }));
    } catch (e) {
      throw new Error(e.message);
    }
  };
}
