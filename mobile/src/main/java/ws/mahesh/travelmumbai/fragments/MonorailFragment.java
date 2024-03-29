package ws.mahesh.travelmumbai.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import ws.mahesh.travelmumbai.MainActivity;
import ws.mahesh.travelmumbai.R;
import ws.mahesh.travelmumbai.local.Base;
import ws.mahesh.travelmumbai.monorail.MonoFareBase;
import ws.mahesh.travelmumbai.monorail.MonoListAdapter;
import ws.mahesh.travelmumbai.monorail.MonoListItem;
import ws.mahesh.travelmumbai.monorail.MonoStations;
import ws.mahesh.travelmumbai.utils.MyTagHandler;
import ws.mahesh.travelmumbai.utils.StationFinder;

/**
 * Created by Mahesh on 7/9/2014.
 */
public class MonorailFragment extends Fragment {
    Spinner source;
    Button moreInfo;
    ImageButton getLoc;
    MonoFareBase mono = new MonoFareBase();

    private List<MonoListItem> monoItem = new ArrayList<MonoListItem>();

    public MonorailFragment() {
        super();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.mono_fare_fragment, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Travel Mumbai - Monorail");

        source = (Spinner) getActivity().findViewById(R.id.spinnerSource);

        moreInfo = (Button) getActivity().findViewById(R.id.buttonmoreInfo);

        getLoc = (ImageButton) getActivity().findViewById(R.id.imageButtonLoc);

        final StationFinder sf = new StationFinder();

        moreInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                alertDialogBuilder.setTitle("Mono Information");
                alertDialogBuilder.setMessage(Html.fromHtml(readFromFile("monorail/timing.tm"), null, new MyTagHandler()));
                alertDialogBuilder.setNeutralButton("Close", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

        source.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                new Thread(new Runnable() {
                    public void run() {
                        setValues();
                    }
                }).start();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        getLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                source.setSelection(sf.getNearbyMonoStation(Base.lastKnownLat, Base.lastKnownLon) + 1);
            }
        });
        source.setSelection(sf.getNearbyMonoStation(Base.lastKnownLat, Base.lastKnownLon) + 1);
    }

    private void setValues() {
        int current = source.getSelectedItemPosition();
        if (current > 0)
            calculateFare(current - 1);
    }

    private void calculateFare(int src) {
        monoItem.clear();
        monoItem.add(new MonoListItem("Destination", "Token Fare", "Card Fare"));
        for (int i = 0; i < MonoStations.COUNT; i++) {
            monoItem.add(new MonoListItem(MonoStations.STATIONS[i], "" + mono.getTokenFare(src, i), "" + mono.getCardFare(src, i)));
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MonoListAdapter adapter = new MonoListAdapter(getActivity(), R.layout.metro_mono_list_item, monoItem);
                ListView list = (ListView) getActivity().findViewById(R.id.listView1);
                list.setAdapter(adapter);
            }
        });

    }

    private String readFromFile(String fname) {
        String str = "";
        try {
            InputStream in = getActivity().getAssets().open(fname);
            int size = in.available();
            byte[] buffer = new byte[size];
            in.read(buffer);
            in.close();
            str = new String(buffer);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }

}