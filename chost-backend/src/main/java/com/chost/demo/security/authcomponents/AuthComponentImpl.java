package com.chost.demo.security.authcomponents;

import com.chost.demo.controller.exceptions.IllegalAccessException;
import com.chost.demo.model.entity.File;
import com.chost.demo.model.entity.User;
import com.chost.demo.model.repository.FileRepository;
import com.chost.demo.model.wrappers.UserWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AuthComponentImpl implements AuthComponent {
    @Autowired
    private FileRepository fileRepository;
    private static  final Logger LOGGER = LoggerFactory.getLogger(AuthComponentImpl.class);

    @Override
    public boolean canBuyFile(String fileId, UserWrapper wrapper) {
        try {
            File file = fileRepository.findById(Integer.parseInt(fileId)).orElse(null);
            if (!file.isWhiteListed()){
                return true;
            }
            LOGGER.info(wrapper.getLogin() + " tries to buy " + file.getName() + "with ID:"+ file.getId() +" file");
            boolean equals = false;
            for (User u: file.getWhiteList()
                 ) {
                if (u.getLogin().equals(wrapper.getLogin())){
                    equals = true;
                }

            }
            if (equals){
                LOGGER.info(wrapper.getLogin() + " successfully bought file with id = "+file.getId() );
                return true;
            } else {
                LOGGER.error("Illegal access to ID: "+file.getId()+" from "+wrapper.getLogin()+" denied.");
            }

        } catch (Throwable throwable){
            LOGGER.error("Illegal access to unknown file from "+wrapper.getLogin()+" denied.");
        }
        throw new IllegalAccessException("Illegal access!");
    }

    @Override
    public boolean isOwnerOf(String fileId, UserWrapper wrapper) {

        try {
            File file = fileRepository.findById(Integer.parseInt(fileId)).orElse(null);
            LOGGER.info(wrapper.getLogin() + " tries to access " + file.getName() + "with ID:"+ file.getId() +" file");
            boolean equals = wrapper.getLogin().equals(file.getOwner().getLogin());
            if (equals){
                LOGGER.info(wrapper.getLogin() + " successfully accessed file with id = "+file.getId() );
                return true;
            } else {
                LOGGER.error("Illegal access to ID: "+file.getId()+" from "+wrapper.getLogin()+" denied.");
            }

        } catch (Throwable throwable){
            LOGGER.error("Illegal access to unknown file from "+wrapper.getLogin()+" denied.");
        }
        throw new IllegalAccessException("Illegal access!");
    }
}
