import { PropsWithChildren, useState } from "react";
import NavBar from "../components/dashboard-components/navbar";
import SideBar from "../components/dashboard-components/sidebar";

export default function LayoutDashboard(props: PropsWithChildren) {
  const [sideBarOpen, setSideBarOpen] = useState(false);

  return (
    <>
      <div className="relative" id="dashboard">
        <div
          aria-hidden="true"
          className="absolute inset-0 grid grid-cols-2 -space-x-52 opacity-40 dark:opacity-20"
        >
          <div className="blur-[106px] h-56 bg-gradient-to-br from-primary to-purple-400 dark:from-blue-700"></div>
          <div className="blur-[106px] h-32 bg-gradient-to-r from-cyan-400 to-sky-300 dark:to-indigo-600"></div>
        </div>
        <div className="leading-normal tracking-normal" id="dashboard-layout">
          <div className="flex flex-wrap">
            <SideBar isOpen={sideBarOpen} />
            {/* todo: overlay reactive to sidebar  */}
            <div
              className={
                sideBarOpen
                  ? "w-full  pl-0 lg:pl-64 min-h-screen overlay"
                  : "w-full  pl-0 lg:pl-64 min-h-screen"
              }
            >
              <NavBar setSideBarState={setSideBarOpen} isOpen={sideBarOpen} />
              <div className="p-6 mb-20">{props.children}</div>
            </div>
          </div>
        </div>
      </div>
    </>
  );
}
