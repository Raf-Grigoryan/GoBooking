package org.example.gobookingcommon.customException;

public class SlotAlreadyBookedException extends RuntimeException{
    public SlotAlreadyBookedException(String message) {
        super(message);
    }
}
