/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package varastonhallinta.security;

import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import varastonhallinta.domain.Role;
import varastonhallinta.domain.User;
import varastonhallinta.logic.RoleJpaController;
import varastonhallinta.logic.UserJpaController;

/**
 *
 * @author tanel
 */
public class Authenticator {

    private UserJpaController userController;
    
    /**
     *
     * @param userController
     */
    public Authenticator(UserJpaController userController){
        this.userController = userController;
    }
    
    /**
     *
     * @param username
     * @param password
     * @return
     */
    public boolean validate(String username, String password){
        System.out.println("validate " + username);
        User user;
        try{
            user = userController.findUserWithName(username);
        }catch(NoResultException e){
            user = null;
        }
        return user != null && user.getPassword().equals(password);
    }
}