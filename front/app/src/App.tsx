import "./App.css";
import Index from "./pages";
import { RouterProvider, createBrowserRouter } from "react-router-dom";
import SignIn from "./pages/sign-in";
import SignUp from "./pages/sign-up";
import Dashboard from "./pages/dashboard";
import QuizList from "./pages/quizList";
import QuizStart from "./pages/quizStart";
import QuizContent from "./pages/quizContent";
import Quiz from "./pages/quiz";
import QuizResults from "./pages/quizResults";
import UserContextProvider from "./context/user/user.context";

function App() {
  const router = createBrowserRouter([
    {
      path: "/",
      element: <Index />,
    },
    {
      path: "/sign-in", 
      element: <SignIn />,
    },
    {
      path: "/sign-up",
      element: <SignUp />,
    },
    {
      path: "/dashboard",
      element: <Dashboard />,
    },
    {
      path: "/quizList",
      element: <QuizList />,
    },
    {
      path: "/quizStart",
      element: <QuizStart />,
    },
    {
      path: "/quizContent",
      element: <QuizContent />,
    },
    {
      path: "/quiz",
      element: <Quiz />,
    },
    {
      path: "/quizResults",
      element: <QuizResults />,
    },
  ]);
  return (
    <>
      <UserContextProvider>
        <RouterProvider router={router} />
      </UserContextProvider>
    </>
  );
}

export default App;
