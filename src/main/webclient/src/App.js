import logo from './logo.svg';
import './App.css';
import Login from "./components/Login.js";

function App() {
  return (
        <div>
      <Nav />

      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/status" element={<Pricing />} />
        <Route path="/statistics" element={<Dashboard />} />
        <Route path="/traces" element={<Settings />} />
        <Route path="/login" element={<Login />} />
      </Routes>
    </div>
  );
}

export default App;
