import AdminDashboardCard from "./AdminDashboardCard.jsx"

const AltAdminDashboard = () => {
    return (
        <div className="w-full bg-slate-200 grid lg:grid-cols-4 sm:grid-cols-2 grid-cols-1">
            <AdminDashboardCard name="Venues" basePath="venues" showSeeAll={true} />
            <AdminDashboardCard name="Productions" basePath="productions" showSeeAll={true} />
            <AdminDashboardCard name="Festivals" basePath="festivals" showSeeAll={true} />
            <AdminDashboardCard name="Performances" basePath="performances" showSeeAll={false} />
            <AdminDashboardCard name="People" basePath="people" showSeeAll={true} />
            <AdminDashboardCard name="Credits" basePath="credits" showSeeAll={false} />
            <AdminDashboardCard name="Awards" basePath="awards" showSeeAll={false} />
        </div>
    )
}

export default AltAdminDashboard