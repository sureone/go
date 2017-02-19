package com.sureone.igs;
public class IgsMessage {
    /*
     * These message types are put at the beginning of every line
     * of output from the server, with one exception (of course).
     * Some things to be aware of:
     *     If you get an unknown message type, you should probably just
     *     treat it is 'INFO'.  I would guess the same would be true
     *     if there is an error parsing date.  (It would probably be
     *     good to tell the user something is wrong as well.)
     *
     *     To start looking for client messages look for the version message.
     *     If should see:  39 ....  If you see:  IGS entry on mm - dd - yyyy,
     *     you are not in client mode.  But the version string is a way to
     *     tell if you should be looking for IGS messages, as opposed to
     *     terminal messages.
     *
     *     The IGS client protocol is designed to be a line-by-line protocol,
     *     just like telnet is a line-by-line protocol.
     *
     *     Any IGS output, which is INFO is subject to change.
     */

    public final static int	UNKNOWN	=  0;
    public final static int	AUTOMAT	= 35; 	/* Automatch anouncement*/
    public final static int	AUTOASK	= 36; 	/* Automatch accept	*/
    public final static int	CHOICES = 38; 	/* game choices		*/
    public final static int	CLIVRFY = 41;	/* Client verify message */
    public final static int	BEEP	=  2; 	/* \7 telnet 		*/
    public final static int	BOARD	=  3;	/* Board being drawn 	*/
    public final static int	DOWN	=  4;	/* The server is going down */
    public final static int	ERROR	=  5;	/* An error reported	*/
    public final static int    FIL	=  6;	/* File being sent	*/
    public final static int	GAMES	=  7;	/* Games listing	*/
    public final static int    HELP	=  8;	/* Help file		*/
    public final static int	INFO	=  9;	/* Generic info		*/
    public final static int	LAST	= 10;	/* Last command		*/
    public final static int KIBITZ	= 11;	/* Kibitz strings	*/
    public final static int	LOAD	= 12;	/* Loading a game	*/
    public final static int	LOOK_M	= 13;	/* Look 		*/
    public final static int MESSAGE	= 14;	/* Message lising	*/
    public final static int MOVE	= 15;	/* Move #:(B) A1	*/
    public final static int	OBSERVE	= 16;	/* Observe report	*/
    public final static int PROMPT	=  1;	/* A Prompt (never)	*/
    public final static int	REFRESH	= 17;	/* Refresh of a board	*/
    public final static int SAVED	= 18;	/* Stored command	*/
    public final static int SAY	= 19;	/* Say string		*/
    public final static int SCORE_M	= 20;	/* Score report		*/
    public final static int SGF_M	= 34;	/* SGF variation	*/
    public final static int SHOUT	= 21;	/* Shout string		*/
    public final static int SHOW 	= 29;	/* Shout string		*/
    public final static int STATUS	= 22;	/* Current Game status	*/
    public final static int	STORED	= 23;	/* Stored games		*/
    public final static int TEACH	= 33;	/* teaching game	*/
    public final static int TELL	= 24;	/* Tell string		*/
    public final static int DOT	= 40;	/* your . string	*/
    public final static int	THIST	= 25;	/* Thist report		*/
    public final static int	TIM	= 26;	/* times command	*/
    public final static int	TRANS	= 30;	/* Translation info	*/
    public final static int	TTT_BOARD= 37; 	/* tic tac toe		*/
    public final static int	WHO	= 27;	/* who command		*/
    public final static int	UNDO	= 28;	/* Undo report		*/
    public final static int	USER	= 42;	/* Long user report	<-- last */
    public final static int	VERSION = 39;	/* IGS VERSION: IGS entry on %02d - %02d - %04d\r\n
				 comes out just as the last piece of info
				 after verification of the password.
				 mm - dd - yyyy <== last Message Type value */
    public final static int	YELL	= 32;	/* Channel yelling	*/

    public final static int MATCH = 80;
    public final static int STEP = 81;
    public final static int PASS = 82;
    public final static int MOVES = 83;
    public final static int GAME = 84;
    public final static int STATS = 85;
    public final static int SCORE = 86;
    public final static int MAX = 256;
}
