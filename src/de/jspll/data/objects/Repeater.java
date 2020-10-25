package de.jspll.data.objects;

import de.jspll.data.ChannelID;
import de.jspll.handlers.GameObjectHandler;

import static de.jspll.data.ChannelID.DISPATCH;

/**
 * Created by reclinarka on 25-Oct-20.
 */
public class Repeater extends GameObject{
    GameObjectHandler reciever;
    public Repeater(String ID,GameObjectHandler reciever) {
        super(ID,"g.tst.Repeater");
        this.reciever = reciever;
    }

    public void setReciever(GameObjectHandler reciever) {
        this.reciever = reciever;
    }

    @Override
    public char call(Object[] input) {
        if(input[0].getClass() == ChannelID.class){
            reciever.dispatch((ChannelID) input[0],(Object[]) input[1]);
            return 0;
        }
        return super.call(input);
    }

    @Override
    public ChannelID[] getChannels() {
        return new ChannelID[]{DISPATCH};
    }
}
