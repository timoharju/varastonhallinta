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

public class Authenticator {
    private static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("Varastonhallinta_hib-0.0.1-SNAPSHOT.jar");
    private static UserJpaController userController = new UserJpaController(entityManagerFactory);
    private static RoleJpaController roleController = new RoleJpaController(entityManagerFactory);

    static{
        Role admin = new Role("Admin");
        Role user = new Role("User");
        Role editor = new Role("Editor");
        roleController.create(admin);
        roleController.create(user);
        roleController.create(editor);
        userController.create(new User("admin", "admin", admin));
        userController.create(new User("user", "user", user));
        userController.create(new User("editor", "editor", editor));
    }
    
    public static boolean validate(String username, String password){
        System.out.println("validate " + username);
        try{
            User user = userController.findUserWithName(username);
        }catch(NoResultException e){
            return false;
        }
        return true;
    }
}