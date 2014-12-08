package com.clemble.casino.server.phone.service;

import com.clemble.casino.player.service.PlayerPhoneService;
import com.clemble.casino.server.phone.PendingPlayerPhone;
import com.clemble.casino.server.phone.PlayerPhone;
import com.clemble.casino.server.phone.repository.PendingPlayerPhoneRepository;
import com.clemble.casino.server.phone.repository.PlayerPhoneRepository;

/**
 * Created by mavarazy on 12/8/14.
 */
public class ServerPlayerPhoneService implements PlayerPhoneService {

    final private PhoneCodeGenerator codeGenerator;
    final private ServerSMSSender serverSMSSender;
    final private PlayerPhoneRepository phoneRepository;
    final private PendingPlayerPhoneRepository pendingPlayerPhoneRepository;

    public ServerPlayerPhoneService(PhoneCodeGenerator codeGenerator, ServerSMSSender serverSMSSender, PlayerPhoneRepository phoneRepository, PendingPlayerPhoneRepository pendingPlayerPhoneRepository) {
        this.codeGenerator = codeGenerator;
        this.serverSMSSender = serverSMSSender;
        this.phoneRepository = phoneRepository;
        this.pendingPlayerPhoneRepository = pendingPlayerPhoneRepository;
    }

    @Override
    public boolean add(String phone) {
        throw new UnsupportedOperationException();
    }

    public boolean add(String me, String phone) {
        String code = codeGenerator.generateCode();
        // Step 1. Creating Pending player phone record
        pendingPlayerPhoneRepository.save(new PendingPlayerPhone(me, phone, code));
        // Step 2. Sending verification code
        serverSMSSender.send(phone, "Code: " + code);
        // Step 3. Returning true
        return true;
    }

    public boolean remove(){
        throw new UnsupportedOperationException();
    }

    public boolean remove(String me) {
        // Step 1. Removing pending phone
        pendingPlayerPhoneRepository.delete(me);
        // Step 2. Removing phone
        phoneRepository.delete(me);
        // Step 3. Returning true
        return true;
    }

    @Override
    public boolean verify(String code) {
        throw new UnsupportedOperationException();
    }

    public boolean verify(String me, String code) {
        // Step 1. Getting pending player phone
        PendingPlayerPhone pendingPlayerPhone = pendingPlayerPhoneRepository.findOne(me);
        if(!pendingPlayerPhone.getCode().equals(code))
            return false;
        // Step 2. Adding player phone
        PlayerPhone playerPhone = new PlayerPhone(pendingPlayerPhone.getPlayer(), pendingPlayerPhone.getPhone());
        // Step 3. Saving player phone
        phoneRepository.save(playerPhone);
        // Step 4. Returning true
        return true;
    }

}
