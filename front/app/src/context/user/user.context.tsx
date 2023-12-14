import React, {PropsWithChildren, createContext, useState } from "react";
import {User} from '../../libs/user';

type UserContextType = {
    user:User|null,
    setUser (value:User|null): void,
}

export const UserContext:React.Context<UserContextType> = createContext<UserContextType>({
    user:null,
    setUser:(value:User|null)=>{}
});


export default function UserContextProvider(props:PropsWithChildren){{
    const [user,setUser] = useState<User|null>(null);
    return(
        <UserContext.Provider value={{user,setUser}}>
            {props.children}
        </UserContext.Provider>
    );
}}





