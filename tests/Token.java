import eu.nerdz.api.impl.reverse.ReverseLoginData;
import eu.nerdz.api.impl.reverse.messages.ReverseMessenger;
import eu.nerdz.api.messages.Conversation;
import eu.nerdz.api.messages.ConversationHandler;
import eu.nerdz.api.messages.Message;

public class Token {

    public static void main(String[] args) {

        try {

			if (args.length != 3) {
				System.err.println("usage: <classinvocation> username userid token");
				return;
			}
				

            ReverseMessenger messenger = new ReverseMessenger(new ReverseLoginData(args[0], args[1], args[2]));
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
