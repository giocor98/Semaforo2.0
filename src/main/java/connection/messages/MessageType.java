package connection.messages;

import static java.lang.String.format;

/**
 * Enumeration containing all the types of message available.
 * It offers the possibility to build the right Message for each
 * message Type.
 *
 * @see Message
 */
public enum MessageType {
    /**
     * ping message.
     */
    PING(){
        /**
         * Method to return the ping message.
         *
         * @return (a ping message).
         */
        public Message build(){
            return new Message("p");
        }
    },
    /**
     * Message to set and show the name of the pilot.
     */
    PILOT_NAME{
        /**
         * Method to get the correct <code>PILOT_NAME</code>
         * <code>Message</code>.
         *
         * @param name (the name of the pilot as a <code>String</code>).
         * @param lane (the number of the lane on which the pilot is running).
         * @return  (the desired <code>PILOT_NAME</code> <code>Message</code>).
         */
        public Message build(String name, int lane){
            return new Message(format("p%d%s|", lane, name));
        }
    },
    /**
     * Message to write on the selected lane.
     */
    WRITE_ON_LANE{
        /**
         * Method to get the desired <code>WRITE_ON_LANE</code>
         * <code>Message</code>. It permits to choose the lane
         * on which to write and the content for both line 1
         * and line 2 of the screen.
         *
         * @param line1 (the <code>String</code> to be written
         *              on the first line of the screen).
         * @param line2 (the <code>String</code> to be written
         *              on the second line of the screen).
         * @param lane (the <code>int</code> denoting the lane
         *             on which it has to write the message).
         * @return     (the correct <code>Message</code>).
         */
        public Message build(String line1, String line2, int lane) {
            return new Message(format("w%d%s|%s|", lane, line1, line2));
        }
    },
    /**
     * Message to write on all the lanes' screens.
     */
    WRITE_ON_ALL{
        /**
         * Method to get the desired <code>WRITE_ON_ALL</code>
         * <code>Message</code>. It permits to set the write
         * to be shown on all the screens with both upper and
         * lower line write.
         *
         * @param upperString (the <code>String</code> to be
         *                    written on the upper parts of
         *                    the screens).
         * @param lowerString (the <cose>String </cose> to be
         *                    written on the lower parts of
         *                    the screens).
         * @return            (the correct <code>Message</code>).
         */
        public Message build(String upperString, String lowerString){
            return new Message(format("W%s|%s|", upperString, lowerString));
        }
    },
    /**
     * Message to set the lap timeout.
     */
    LAP_TIMEOUT{
        /**
         * Method to get the desired <code>LAP_TIMEOUT</code>
         * <code>Message</code> with the desired time in millis.
         *
         * @param timeOut (the number of millis o be set as lap timeout).
         * @return        (the correct <code>Message</code>).
         */
        public Message build(int timeOut){
            return new Message(format("t%d", timeOut));
        }
    },
    /**
     * Message to set the timeout on the start.
     */
    START_TIMEOUT{
        /**
        * Method to get the desired <code>START_TIMEOUT</code>
        * <code>Message</code> with the desired time in millis.
        *
        * @param timeOut (the number of millis o be set as start timeout).
        * @return        (the correct <code>Message</code>).
        */
        public Message build (int timeOut){
            return new Message(format("T%d", timeOut));
        }
    },
    /**
     * Method to set and display the race name.
     */
    RACE_NAME{
        /**
         * Method to get the desired <code>RACE_NAME</code>
         * <code>Message</code> with the desired name of
         * the race to be shown on all the screens.
         *
         * @param name (the name of the race).
         * @return     (the correct <code>Message</code>).
         */
        public Message build (String name){
            return new Message(format("h %s|",name));
        }
    },
    /**
     * Message to return to the HOME screen.
     */
    HOME{
        /**
         * Method to get the desired <code>HOME</code>
         * <code>Message</code>.
         *
         * @return (the correct <code>Message</code>).
         */
        public Message build(){
            return new Message("H");
        }
    },
    /**
     * Message to set the running lane interchange clockwise.
     */
    CLOCKWISE_RUN {
        /**
         * Method to get the desired <code>CLOCKWISE_RUN</code>
         * <code>Message</code>.
         *
         * @return (the correct <code>Message</code>).
         */
        public Message build (){
            return new Message("c");
        }
    },
    /**
     * Message to set the running lane interchange counterclockwise.
     */
    COUNTERCLOCKWISE_RUN{
        /**
         * Method to get the desired <code>COUNTERCLOCKWISE_RUN</code>
         * <code>Message</code>.
         *
         * @return (the correct <code>Message</code>).
         */
        public Message build(){
            return new Message("C");
        }
    },
    /**
     * Message to start the semaphore.
     */
    START{
        /**
         * Method to get the desired <code>START</code>
         * <code>Message</code>.
         *
         * @return (the correct <code>Message</code>).
         */
        public Message build(){
            return new Message("S");
        }
    }
}
