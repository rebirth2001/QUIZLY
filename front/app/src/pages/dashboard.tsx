import { Link } from "react-router-dom";
import LayoutDashboard from "../layouts/layout-dashboard";
import { useContext } from "react";
import { UserContext } from "../context/user/user.context";

export default function Dashboard() {
  const userContext = useContext(UserContext);
  return (
    <>
      {userContext.user != null ? (
        <LayoutDashboard>
          <div id="home">
            <div className="lg:flex justify-between items-center mb-6">
              <p className="text-2xl font-semibold mb-2 lg:mb-0">
                Good afternoon, {userContext.user.getUsername()}!
              </p>
              <Link to={"/quizStart"}>
                <button className="bg-blue-500 hover:bg-blue-600 focus:outline-none rounded-lg px-6 py-2 text-white font-semibold shadow">
                  Create a quizz
                </button>
              </Link>
            </div>
          </div>
        </LayoutDashboard>
      ) : (
        <div className="hero min-h-screen bg-base-200">
          <div className="hero-content text-center">
            <div className="max-w-md">
              <h1 className="text-5xl font-bold">You Should Log-In first</h1>
              <p className="py-6">
                Please sign in to access the quiz creator dashboard.
              </p>
              <Link to={"/sign-in"} className="btn btn-primary">
                Sign in
              </Link>
            </div>
          </div>
        </div>
      )}
    </>
  );
}
