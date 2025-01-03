import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom"
import Login from "./components/Login.jsx"
import Home from "./components/Home.jsx"
import './App.css'

function App() {
  

  return (
    <Router>
      <Routes>
        <Route path="/" element={<Navigate to = "/home" replace/>}>

        </Route>
        <Route path="/home" element={<Home />} >
        </Route>
        <Route path = "/login" element = {<Login/>}>

        </Route>
      </Routes>
    </Router>
  )
}

export default App
