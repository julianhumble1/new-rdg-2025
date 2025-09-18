import {
  BuildingLibraryIcon,
  FilmIcon,
  ScaleIcon,
  StarIcon,
  TicketIcon,
  TrophyIcon,
  UserGroupIcon,
  UsersIcon,
} from "@heroicons/react/16/solid";
import ContentCard from "../common/ContentCard.jsx";
import AdminDashboardCard from "./AdminDashboardCard.jsx";

const AltAdminDashboard = () => {
  return (
    <ContentCard>
      <div className="grid sm:grid-cols-3 grid-cols-1">
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
      </div>
    </ContentCard>
  );
};

export default AltAdminDashboard;
