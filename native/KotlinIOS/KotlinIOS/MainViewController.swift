import UIKit
import SharedCode

class MainViewController: UIViewController {

    @IBOutlet private var fromButton: UIButton!
    @IBOutlet private var toButton: UIButton!
    @IBOutlet private var button: UIButton!
    @IBOutlet private var resultsTable: UITableView!
    
    private var stations: [Station] = [Station]()
    private var fromSelected: Station!
    private var toSelected: Station!
    private var journeyCollection: JourneyCollection!
    
    private let presenter: ApplicationContractPresenter = ApplicationPresenter()
    
    required init?(coder: NSCoder) {
        super.init(coder: coder)
        NotificationCenter.default.addObserver(self, selector: #selector(didGetSelectedStationNotification(notification:)), name: Notification.Name("StationsSelected"), object: nil)
    }
    
    @objc func didGetSelectedStationNotification(notification: Notification) {
        let selectionReturn = notification.object as! SelectionReturn
        if (selectionReturn.type == "Departure station") {
            fromSelected = selectionReturn.stationSelected
            fromButton.setTitle(fromSelected.stationName, for: .normal)
            fromButton.titleLabel?.textAlignment = NSTextAlignment.center
        }
        else{
            toSelected = selectionReturn.stationSelected
            toButton.setTitle(toSelected.stationName, for: .normal)
            toButton.titleLabel?.textAlignment = NSTextAlignment.center
        }
    }
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        presenter.onViewTaken(view: self)
        fromButton.layer.borderColor = UIColor.darkGray.cgColor
        toButton.layer.borderColor = UIColor.darkGray.cgColor
        button.setTitle("Loading...", for: .disabled)
        
    }
    
    @IBAction func sendSelection() {
        presenter.runSearch(from: fromSelected, to: toSelected)
    }
    
    @IBAction func launchDepartureSearch() {
        launchSearchActivity(title: "Departure station")
    }
    
    @IBAction func launchArrivalSearch() {
        launchSearchActivity(title: "Arrival station")
    }
    
    func launchSearchActivity(title: String) {
        performSegue(withIdentifier: "SearchViewSegue", sender: self)
        NotificationCenter.default.post(name: Notification.Name("SearchViewTitle"), object: title)
        NotificationCenter.default.post(name: Notification.Name("SearchViewStations"), object: stations)
    }
    
    
}

extension MainViewController: ApplicationContractView {
    func saveStations(stations: [Station]) {
        self.stations = stations
    }
    
    func disableSearchButton() {
        button.isEnabled = false
        toButton.isEnabled = false
        fromButton.isEnabled = false
    }
    
    func enableSearchButton() {
        button.isEnabled = true
        toButton.isEnabled = true
        fromButton.isEnabled = true
    }
    
    func setTitle(title: String) {
        self.title = title
        self.navigationController!.navigationBar.titleTextAttributes = [NSAttributedString.Key.font: UIFont.systemFont(ofSize: 25), NSAttributedString.Key.foregroundColor: UIColor.white]
    }
    
    func displayErrorMessage(message: String) {
        let alert = UIAlertController(title: "Error", message: message, preferredStyle: .alert)
        alert.addAction(UIAlertAction(title: "OK", style: .default))
        self.present(alert, animated: true, completion: nil)
    }
    
    func displayJourneys(journeyCollection: JourneyCollection) {
        self.journeyCollection = journeyCollection
        resultsTable.dataSource = self
        resultsTable.reloadData()
        resultsTable.rowHeight = UITableView.automaticDimension
    }
    
    func openUrl(url: String) {
        if let page = URL(string: url) {
            UIApplication.shared.open(page)
        }
    }
    
    func setStations(stations: [Station]) {}
}

extension MainViewController : UITableViewDataSource {
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "results_item", for: indexPath) as! ResultsTableCell
        let journey = journeyCollection.outboundJourneys[indexPath.item]
        
        cell.departureTime.text = journey.departureTimeFormatted
        cell.arrivalTime.text = journey.arrivalTimeFormatted
        cell.departureExtraDay.text = journey.departureExtraDay
        cell.arrivalExtraDay.text = journey.arrivalExtraDay
        cell.departureStation.text = journey.originStation.displayName
        cell.arrivalStation.text = journey.destinationStation.displayName
        cell.status.text = journey.status.statusText
        let backgroundColor = journey.status.backgroundColor
        cell.status.backgroundColor = UIColor(red: toCGFloat(component: backgroundColor.red), green: toCGFloat(component: backgroundColor.green), blue: toCGFloat(component: backgroundColor.blue), alpha: toCGFloat(component: backgroundColor.alpha))
        cell.arrow.tintColor = UIColor(red: toCGFloat(component: backgroundColor.red), green: toCGFloat(component: backgroundColor.green), blue: toCGFloat(component: backgroundColor.blue), alpha: toCGFloat(component: backgroundColor.alpha))
        let textColor = journey.status.textColor
        cell.status.textColor = UIColor(red: toCGFloat(component: textColor.red), green: toCGFloat(component: textColor.green), blue: toCGFloat(component: textColor.blue), alpha: toCGFloat(component: textColor.alpha))
        cell.status.textContainerInset = UIEdgeInsets(top: 5, left: 1, bottom: 5, right: 1);
        
        return cell
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return journeyCollection.outboundJourneys.count
    }
    
    func toCGFloat(component: Int32) -> CGFloat {
        return CGFloat(Double(component) / 256.0)
    }
}


