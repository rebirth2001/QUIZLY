import LayoutMain from "../layouts/layout-main";
import Container from "../components/containter";
import { useContext, useState } from "react";
import { useNavigate } from "react-router-dom";
import { authenticateUser,AuthenticateRequest, User } from "../libs/user";
import { UserContext } from "../context/user/user.context";

export default function SignIn() {  
  const userContext = useContext(UserContext);
  const navigate = useNavigate();

  const [email, setEmail] = useState<string>("");
  const [password, setPassword] = useState<string>("");
  const [errMsg, setErrMsg] = useState<string>("");

  const handleSubmit = async (e: React.FormEvent) => {
    setErrMsg('');
    e.preventDefault();
    const req:AuthenticateRequest = {
      email,
      password
    }
    const response = await authenticateUser(req);
    if(response != null && response.accessToken.length >0 && response.success == true){
      const user = new User(response.accessToken)
      await user.updateUserDetails();
      userContext.setUser(user);
      navigate("/dashboard");
    }else{
      setErrMsg("Wrong email or password.");
    }
  }

  if(userContext.user != null){
    navigate("/dashboard");
  }

  return (
        <LayoutMain>
          <div
            aria-hidden="true"
            className="absolute inset-0 grid grid-cols-2 -space-x-52 opacity-40 dark:opacity-20"
          >
            <div className="blur-[106px] h-56 bg-gradient-to-br from-primary to-purple-400 dark:from-blue-700"></div>
            <div className="blur-[106px] h-32 bg-gradient-to-r from-cyan-400 to-sky-300 dark:to-indigo-600"></div>
          </div>
          <Container>
            <div className="relative pt-36 ml-auto flex justify-center py-12">
              <div className="px-8 py-8 rounded bg-white w-2/5 shadow">
                <h1 className="mb-4 text-3xl font-bold text-gray-900 text-center">
                  Welcome back
                </h1>
                <div role="alert" className={`alert alert-error ${errMsg ? '' : 'hidden'}`}>
                  <svg xmlns="http://www.w3.org/2000/svg" className="stroke-current shrink-0 h-6 w-6" fill="none" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M10 14l2-2m0 0l2-2m-2 2l-2-2m2 2l2 2m7-2a9 9 0 11-18 0 9 9 0 0118 0z" /></svg>
                  <span>{errMsg}</span>
                </div>
                <form onSubmit={handleSubmit}>
                  <div className="py-2">
                    <label
                      htmlFor="Email"
                      className="text-sm font-semibold text-gray-800"
                    >
                      Email
                    </label>
                    <input
                      id="Email"
                      type="email"
                      name="email"
                      placeholder="quizly@example.com"
                      required
                      autoComplete="email"
                      className="w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-800 rounded"
                      value={email}
                      onChange={(e) => setEmail(e.target.value)}
                    />
                  </div>
                  <div className="py-2">
                    <label
                      htmlFor="Password"
                      className="text-sm font-semibold text-gray-800"
                    >
                      Password
                    </label>
                    <input
                      id="Password"
                      type="password"
                      name="password"
                      placeholder="password"
                      required
                      className="w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-800 rounded"
                      value={password}
                      onChange={(e) => setPassword(e.target.value)}
                    />
                  </div>
                  <div className="py-2 flex justify-center">
                    <button
                      className="w-full py-4 px-2 rounded bg-indigo-600 text-white hover:bg-indigo-700 font-bold text-lg"
                      type="submit"
                    >
                      Sign In
                    </button>
                  </div>
                  <div className="py-2 flex justify-between w-full text-xs text-gray-500">
                    <p>
                      Don't have an account?{" "}
                      <a href="/sign-up" className="underline">
                        Sign up
                      </a>
                    </p>
                    <p>
                      Forgot password?{" "}
                      <a href="/reset-pw" className="underline">
                        Reset password
                      </a>
                    </p>
                  </div>
                </form>
              </div>
            </div>
          </Container>
        </LayoutMain>
      )
}