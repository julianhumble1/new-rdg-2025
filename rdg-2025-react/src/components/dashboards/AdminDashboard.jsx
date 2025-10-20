import {
  ArrowRightIcon,
  BuildingLibraryIcon,
  CalendarDaysIcon,
  FilmIcon,
  PhotoIcon,
  ScaleIcon,
  StarIcon,
  TicketIcon,
  TrophyIcon,
  UsersIcon,
} from "@heroicons/react/16/solid";
import ContentCard from "../common/ContentCard.jsx";
import AdminDashboardCard from "./AdminDashboardCard.jsx";
import Button from "../common/Button.jsx";
import { useNavigate } from "react-router-dom";
import EventService from "../../services/EventService.js";

const AdminDashboard = () => {
  const navigate = useNavigate();

  return (
    <ContentCard>
      <div className="grid sm:grid-cols-2 grid-cols-1 lg:grid-cols-3">
        <AdminDashboardCard
          name="Venues"
          basePath="venues"
          showSeeAll={true}
          icon={<BuildingLibraryIcon />}
        />
        <AdminDashboardCard
          name="Productions"
          basePath="productions"
          showSeeAll={true}
          icon={<ScaleIcon />}
        />
        <AdminDashboardCard
          name="Festivals"
          basePath="festivals"
          showSeeAll={true}
          icon={<StarIcon />}
        />
        <AdminDashboardCard
          name="Performances"
          basePath="performances"
          showSeeAll={false}
          icon={<TicketIcon />}
        />
        <AdminDashboardCard
          name="People"
          basePath="people"
          showSeeAll={true}
          icon={<UsersIcon />}
        />
        <AdminDashboardCard
          name="Credits"
          basePath="credits"
          showSeeAll={false}
          icon={<FilmIcon />}
        />
        <AdminDashboardCard
          name="Awards"
          basePath="awards"
          showSeeAll={false}
          icon={<TrophyIcon />}
        />
        <AdminDashboardCard
          name="Images"
          showSeeAll={false}
          showAddNew={false}
          basePath="home/images"
          icon={<PhotoIcon />}
          customButton={
            <Button onClick={() => navigate("/home/images")}>
              Update Home Images
              <ArrowRightIcon className="w-4 h-4 inline ml-2" />
            </Button>
          }
        />
        <AdminDashboardCard
          name="Events"
          showSeeAll
          basePath="events"
          icon={<CalendarDaysIcon />}
        />
      </div>
    </ContentCard>
  );
};

export default AdminDashboard;
