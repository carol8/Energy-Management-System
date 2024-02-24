import {Route, Routes, Navigate} from "react-router-dom";
import "bootstrap/dist/css/bootstrap.min.css";

import Login from "./pages/Login";
import Sike from "./pages/Sike";
import Admin from "./pages/Admin";
import User from "./pages/User";
import Test from "./pages/Test";
import Test2 from "./pages/Test2";
import Chat from "./pages/Chat";

function App() {
    return (
        <Routes>
            <Route path="/" element={<Navigate to="/login"/>}/>
            <Route path="/login" element={<Login/>}/>
            <Route path="/sike" element={<Sike/>}/>
            <Route path="/admin" element={<Admin/>}/>
            <Route path="/user" element={<User/>}/>
            <Route path="/test" element={<Test/>}/>
            <Route path="/test2" element={<Test2/>}/>
            <Route path="/chat" element={<Chat/>}/>
        </Routes>
    );
}

export default App;
