package tutorial.root.ui.activity.mainActivity.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import tutorial.root.R;
import tutorial.root.pojo.response.Attributes;
import tutorial.root.pojo.response.Bus;

public class BusListAdapter extends RecyclerView.Adapter<BusListAdapter.VH> {

    private Context context;
    private List<Bus> busList = new ArrayList<>();

    private BusListAdapterCallback callbackListener;

    public BusListAdapter(Context context) {
        this.context = context;
        if(context instanceof BusListAdapterCallback) callbackListener = (BusListAdapterCallback) context;
    }

    public interface BusListAdapterCallback {
        void showRoute(String busCode, String route);
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_bus_list, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        holder.bind(busList.get(position).getAttributes());
    }

    @Override
    public int getItemCount() {
        return busList.size();
    }

    public void updateBusList (List<Bus> updatedBusList) {
        busList.clear();
        busList.addAll(updatedBusList);
        notifyDataSetChanged();
    }

    class VH extends RecyclerView.ViewHolder {

        private View root;

        private TextView busNumber;

        VH(@NonNull View itemView) {
            super(itemView);
            this.root = itemView;
        }

        @SuppressLint("SetTextI18n")
        private void bind(final Attributes attribute) {
            initializeUI();

            busNumber.setText(attribute.getDisplayRouteCode() + "\n\n" + attribute.getRouteName());

            root.setOnClickListener(view -> callbackListener.showRoute(attribute.getDisplayRouteCode(), attribute.getRouteName()));
        }

        private void initializeUI() {
            busNumber = root.findViewById(R.id.text_view_number_of_bus);
        }

    }

}
