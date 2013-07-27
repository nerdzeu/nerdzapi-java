import eu.nerdz.api.impl.reverse.ReverseMessenger;

public class NewTrial {

    public static void main(String[] args) {

        try {

            Messenger messenger = new ReverseMessenger("username", "passwd");
            ConversationHandler conversationHandler = messenger.getConversationHandler();

            for (Conversation conversation : conversationHandler.getConversations()) {

                System.out.println(conversation.toString() + "\n");
                for(Message message : conversationHandler.getMessagesFromConversation(conversation))
                    System.out.println(message);
                System.out.println();

            }

            messenger.sendMessage("username", "message");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }



}
