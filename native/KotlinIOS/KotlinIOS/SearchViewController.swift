import UIKit
import SharedCode

class SearchViewController: UIViewController {

    @IBOutlet private var filterBar: UISearchBar!
    @IBOutlet private var stationsTable: UITableView!
    
    private var stationsData: [Station] = [Station]()
    private var stationsDisplay: [Station] = [Station]()
    private var filter: String? = ""
    
    private let presenter: SearchContractPresenter = SearchPresenter()
    
    required init?(coder: NSCoder) {
        super.init(coder: coder)
        NotificationCenter.default.addObserver(self, selector: #selector(didGetTitleNotification(notification:)), name: Notification.Name("SearchViewTitle"), object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(didGetStationsNotification(notification:)), name: Notification.Name("SearchViewStations"), object: nil)
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        presenter.onViewTaken(view: self)
        filterBar.delegate = self
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        displayStations(stations: stationsData)
    }
    
    @objc func didGetTitleNotification(notification: Notification) {
        title = notification.object as? String
        setTitle(title: title!)
        self.title = title!
    }
    
    @objc func didGetStationsNotification(notification: Notification) {
        stationsData = notification.object as! [Station]
    }
}

extension SearchViewController: SearchContractView {
    func displayErrorMessage(message: String) {
        let alert = UIAlertController(title: "Error", message: message, preferredStyle: .alert)
        alert.addAction(UIAlertAction(title: "OK", style: .default))
        self.present(alert, animated: true, completion: nil)
    }
    
    func displayStations(stations: [Station]) {
        self.stationsDisplay = stations
        stationsTable.dataSource = self
        stationsTable.reloadData()
        stationsTable.rowHeight = UITableView.automaticDimension
    }
    
    func setTitle(title: String) {
        self.title = title
    }
}

extension SearchViewController : UISearchBarDelegate {
    func searchBar(_ searchBar: UISearchBar, textDidChange searchText: String) {
        filter = searchText
        if (filter != ""){
            presenter.filterStations(filter: searchText, stations: stationsData)
        }
    }
}

extension SearchViewController : UITableViewDataSource {
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "stations_item", for: indexPath) as! StationsTableCell
        let station = stationsDisplay[indexPath.item]
        
        cell.stationName.text = station.stationName
        cell.stationCrs.text = station.crs
        cell.stationNlc.text = station.nlc
        return cell
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return stationsDisplay.count
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let selectedStation = stationsDisplay[indexPath.item]
        
        NotificationCenter.default.post(name: Notification.Name("StationsSelected"), object: selectedStation)
    }
}


