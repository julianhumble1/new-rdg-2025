import { Link } from "react-router-dom"

const Home = () => {
    return (<>
        <div className="text-green-500 text-xl">Home</div>
        <Link to="/login" >
            Login
        </Link>
  
    </>)
}

export default Home