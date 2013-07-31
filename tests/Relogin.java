import eu.nerdz.api.impl.reverse.messages.ReverseMessenger;
import eu.nerdz.api.messages.Conversation;
import eu.nerdz.api.messages.ConversationHandler;
import eu.nerdz.api.messages.Message;

public class Relogin {

    public static void main(String[] args) {

        try {

			if (args.length != 2) {
				System.err.println("usage: <classinvocation> username password");
				return;
			}
				

            ReverseMessenger messenger = new ReverseMessenger(args[0], args[1]);
			messenger = new ReverseMessenger(messenger.getLoginData());
            ConversationHandler conversationHandler = messenger.getConversationHandler();

            for (Conversation conversation : conversationHandler.getConversations()) {

                System.out.println(conversation.toString() + "\n");
                for(Message message : conversationHandler.getMessagesFromConversation(conversation))
                    System.out.println(message);
                System.out.println();

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }



}
